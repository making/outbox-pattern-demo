package com.example.outbox.shipment;

import org.springframework.data.repository.ListCrudRepository;

public interface ShipmentRepository extends ListCrudRepository<Shipment, Long> {

}
