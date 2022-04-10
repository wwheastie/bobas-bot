package com.bobasalliance.bobasbot.discord.configuration;

import java.util.concurrent.TimeUnit;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJsonProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.archaius.DefaultPropertyFactory;
import com.netflix.archaius.api.PropertyRepository;
import com.netflix.archaius.api.config.CompositeConfig;
import com.netflix.archaius.api.exceptions.ConfigException;
import com.netflix.archaius.config.DefaultCompositeConfig;
import com.netflix.archaius.config.EnvironmentConfig;
import com.netflix.archaius.config.SystemConfig;

@Configuration
public class DiscordSpringConfiguration {
	@Bean
	public ObjectMapper getObjectMapper() {
		return new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
	}

	@Bean
	public Client getDefaultClient() {
		return ClientBuilder.newBuilder()
				.register(JacksonJsonProvider.class)
				.connectTimeout(3, TimeUnit.SECONDS)
				.readTimeout(5, TimeUnit.SECONDS)
				.build();
	}

	@Bean
	public PropertyRepository getPropertyRepository() throws ConfigException {
		CompositeConfig config = new DefaultCompositeConfig();
		config.addConfig(EnvironmentConfig.class.getSimpleName(), new EnvironmentConfig());
		config.addConfig(SystemConfig.class.getSimpleName(), new SystemConfig());
		return DefaultPropertyFactory.from(config);
	}
}
