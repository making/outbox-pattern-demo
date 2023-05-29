package com.example.outbox.config;

import javax.sql.DataSource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.amqp.dsl.Amqp;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.jdbc.store.JdbcChannelMessageStore;
import org.springframework.integration.jdbc.store.channel.PostgresChannelMessageStoreQueryProvider;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.support.MessageBuilder;

@Configuration(proxyBeanMethods = false)
public class OutboxConfig {

	@Bean
	public JdbcChannelMessageStore jdbcChannelMessageStore(DataSource dataSource) {
		JdbcChannelMessageStore jdbcChannelMessageStore = new JdbcChannelMessageStore(dataSource);
		jdbcChannelMessageStore.setChannelMessageStoreQueryProvider(new PostgresChannelMessageStoreQueryProvider());
		return jdbcChannelMessageStore;
	}

	@Bean
	public QueueChannel outbox(JdbcChannelMessageStore jdbcChannelMessageStore) {
		return MessageChannels.queue(jdbcChannelMessageStore, "outbox").getObject();
	}

	@Bean
	public MessageHandler amqpHandler(AmqpTemplate amqpTemplate, ObjectMapper objectMapper) {
		final MessageHandler messageHandler = Amqp.outboundAdapter(amqpTemplate)
				.exchangeName("order")
				.routingKey("event")
				.getObject();
		final Logger log = LoggerFactory.getLogger("amqpHandler");
		return message -> {
			final JsonNode payload = objectMapper.convertValue(message.getPayload(), JsonNode.class);
			log.info("Send {}", payload);
			messageHandler.handleMessage(MessageBuilder.createMessage(payload, message.getHeaders()));
		};
	}

	@Bean
	public IntegrationFlow messageRelayFlow(MessageHandler amqpHandler) {
		return IntegrationFlow.from("outbox")
				.handle(amqpHandler, e -> e.poller(poller -> poller.fixedDelay(3_000, 3_000).transactional()))
				.get();
	}

}
