package com.example.outbox.config;

import java.time.Clock;

import am.ik.accesslogger.AccessLogger;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.aop.ObservedAspect;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class AppConfig {
	@Bean
	public ObservedAspect observedAspect(ObservationRegistry observationRegistry) {
		return new ObservedAspect(observationRegistry);
	}

	@Bean
	public Clock systemClock() {
		return Clock.systemUTC();
	}

	@Bean
	public AccessLogger accessLogger() {
		return new AccessLogger(httpExchange -> {
			final String uri = httpExchange.getRequest().getUri().getPath();
			return uri != null && !(uri.equals("/readyz") || uri.equals("/livez") || uri.startsWith("/actuator"));
		});
	}
}
