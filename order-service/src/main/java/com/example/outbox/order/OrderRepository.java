package com.example.outbox.order;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

public interface OrderRepository extends ListCrudRepository<Order, Long> {

	@Modifying
	@Query("UPDATE Order SET status=:status WHERE orderId=:orderId AND status <> :status")
	int updateStatus(Long orderId, OrderStatus status);

}
