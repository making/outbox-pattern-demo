package com.example.outbox.shipment;

import io.micrometer.observation.annotation.Observed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Observed
public class ShipmentService {

	private final ShipmentRepository shipmentRepository;

	private final Logger log = LoggerFactory.getLogger(ShipmentService.class);

	public ShipmentService(ShipmentRepository shipmentRepository) {
		this.shipmentRepository = shipmentRepository;
	}

	public void orderCreated(OrderEvents.Created event) {
		log.info("Created order: {}", event);
		final Shipment shipment = new Shipment();
		shipment.setOrderId(event.orderId());
		shipment.setOrderDate(event.orderDate());
		final Shipment saved = this.shipmentRepository.save(shipment);
		log.info("Create shipment: {}", saved);
	}

	public void orderCancelled(OrderEvents.Cancelled event) {
		log.info("Cancelled order: {}", event);
	}

}
