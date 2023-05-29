package com.example.outbox.order;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class OrderEvents {

	public record Created(Long orderId, BigDecimal amount, OffsetDateTime orderDate) implements Serializable {

		public static Created from(Order order) {
			return new Created(order.getOrderId(), order.getAmount(), order.getOrderDate());
		}
	}

	public record Cancelled(Long orderId, OffsetDateTime cancelDate) implements Serializable {

	}

}
