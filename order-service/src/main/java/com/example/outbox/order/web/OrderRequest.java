package com.example.outbox.order.web;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.ZoneOffset;

import com.example.outbox.order.Order;
import com.example.outbox.order.OrderStatus;

record OrderRequest(BigDecimal amount) {
	Order newOrder(Clock clock) {
		final Order order = new Order();
		order.setAmount(this.amount);
		order.setStatus(OrderStatus.CREATED);
		order.setOrderDate(clock.instant().atOffset(ZoneOffset.UTC));
		return order;
	}
}
