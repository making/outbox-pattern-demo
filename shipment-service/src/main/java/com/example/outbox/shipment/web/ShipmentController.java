package com.example.outbox.shipment.web;

import java.util.List;

import com.example.outbox.shipment.Shipment;
import com.example.outbox.shipment.ShipmentRepository;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShipmentController {
	private final ShipmentRepository shipmentRepository;

	public ShipmentController(ShipmentRepository shipmentRepository) {
		this.shipmentRepository = shipmentRepository;
	}

	@GetMapping(path = "/shipments")
	public List<Shipment> getShipments() {
		return this.shipmentRepository.findAll();
	}
}
