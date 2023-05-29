package com.example.outbox.shipment;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class OrderEvents {
	public record Created(Long orderId, BigDecimal amount,
						  OffsetDateTime orderDate) implements Serializable {

	}

	public record Cancelled(Long orderId,
							OffsetDateTime cancelDate) implements Serializable {

	}
}
