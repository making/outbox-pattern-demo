package com.example.outbox.order;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway
public interface OrderGateway {
	@Gateway(requestChannel = "order.create", replyChannel = "order.create.reply")
	Order placeOrder(Order order);

	@Gateway(requestChannel = "order.cancel")
	void cancelOrder(Long orderId);
}
