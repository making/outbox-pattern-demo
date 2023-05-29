package com.example.outbox.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.autoconfigure.amqp.RabbitTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class AmqpConfig {

	@Bean
	public TopicExchange orderExchange() {
		return new TopicExchange("order");
	}

	@Bean
	public Queue orderEventQueue() {
		return new Queue("order.event");
	}

	@Bean
	public Binding orderEventBinding(Queue queue, TopicExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with("event");
	}

	@Bean
	public Jackson2JsonMessageConverter jsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}

	@Bean
	public RabbitTemplateCustomizer rabbitTemplateCustomizer() {
		return rabbitTemplate -> rabbitTemplate.setObservationEnabled(true);
	}

}
