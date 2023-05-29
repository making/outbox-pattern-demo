package com.example.outbox.order;

import io.micrometer.observation.annotation.Observed;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Observed
public class OrderService {
	private final OrderRepository orderRepository;

	public OrderService(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	public Order create(Order order) {
		return this.orderRepository.save(order);
	}

	public int cancel(Long orderId) {
		return this.orderRepository.updateStatus(orderId, OrderStatus.CANCELLED);
	}
}
