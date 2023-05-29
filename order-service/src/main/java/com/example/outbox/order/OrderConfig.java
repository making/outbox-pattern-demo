package com.example.outbox.order;

import java.time.Clock;
import java.time.ZoneOffset;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;

@Configuration(proxyBeanMethods = false)
public class OrderConfig {

	@Bean
	public IntegrationFlow orderCreateFlow(OrderService orderService) {
		return IntegrationFlow.from("order.create")
				.routeToRecipients(routes -> routes.transactional()
						.recipientFlow(f -> f
								.<Order>handle((order, headers) -> orderService.create(order))
								.channel(c -> c.publishSubscribe("order.create.reply"))
								.transform(OrderEvents.Created::from)
								.enrichHeaders(h -> h.header("eventType", "order_created"))
								.channel("outbox")))
				.get();
	}

	@Bean
	public IntegrationFlow orderCancelFlow(OrderService orderService, Clock clock) {
		return IntegrationFlow.from("order.cancel")
				.routeToRecipients(routes -> routes.transactional()
						.recipientFlow(f -> f
								.<Long>handle((orderId, headers) -> {
									final int updated = orderService.cancel(orderId);
									return updated > 0 ? orderId : null;
								})
								.<Long, OrderEvents.Cancelled>transform(orderId -> new OrderEvents.Cancelled(orderId, clock.instant().atOffset(ZoneOffset.UTC)))
								.enrichHeaders(h -> h.header("eventType", "order_cancelled"))
								.channel("outbox")))
				.get();
	}
}
