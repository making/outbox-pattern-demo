package com.example.outbox.shipment;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.observation.annotation.Observed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
@Observed
public class OrderListener {
	private final ShipmentService shipmentService;

	private final ObjectMapper objectMapper;

	private final Logger log = LoggerFactory.getLogger(OrderListener.class);

	public OrderListener(ShipmentService shipmentService, ObjectMapper objectMapper) {
		this.shipmentService = shipmentService;
		this.objectMapper = objectMapper;
	}

	@RabbitListener(queues = "order.event")
	public void handleOrderEvent(JsonNode payload, @Header("eventType") String eventType) {
		switch (eventType) {
			case "order_created" -> {
				final OrderEvents.Created event = this.objectMapper.convertValue(payload, OrderEvents.Created.class);
				this.shipmentService.orderCreated(event);
			}
			case "order_cancelled" -> {
				final OrderEvents.Cancelled event = this.objectMapper.convertValue(payload, OrderEvents.Cancelled.class);
				this.shipmentService.orderCancelled(event);
			}
			default -> log.warn("Unknown Event Type: {}", eventType);
		}
	}
}
