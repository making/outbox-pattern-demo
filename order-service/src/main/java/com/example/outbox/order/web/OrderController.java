package com.example.outbox.order.web;

import java.time.Clock;

import com.example.outbox.order.Order;
import com.example.outbox.order.OrderGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {
	private final OrderGateway orderGateway;

	private final Clock clock;

	private final Logger log = LoggerFactory.getLogger(OrderController.class);

	public OrderController(OrderGateway orderGateway, Clock clock) {
		this.orderGateway = orderGateway;
		this.clock = clock;
	}

	@PostMapping(path = "/orders")
	public Order placeOrder(@RequestBody OrderRequest orderRequest) {
		final Order newOrder = orderRequest.newOrder(this.clock);
		final Order order = this.orderGateway.placeOrder(newOrder);
		log.info("Created {}", order);
		return order;
	}

	@DeleteMapping(path = "/orders/{orderId}")
	public void cancelOrder(@PathVariable Long orderId) {
		this.orderGateway.cancelOrder(orderId);
		log.info("Cancelled {}", orderId);
	}
}
