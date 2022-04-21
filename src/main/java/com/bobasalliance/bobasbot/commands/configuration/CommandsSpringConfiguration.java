package com.bobasalliance.bobasbot.commands.configuration;

import javax.sql.DataSource;

import org.apache.commons.text.similarity.JaroWinklerDistance;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.bobasalliance.bobasbot.discord.configuration.Settings;

@Configuration
public class CommandsSpringConfiguration {
	@Bean
	public DataSource getDataSource(final Settings settings) {
		return DataSourceBuilder.create()
				.url(settings.getDatabaseUrl())
				.username(settings.getDatabaseUser())
				.password(settings.getDatabasePassword())
				.build();
	}

	@Bean
	public JaroWinklerDistance getJaroWinklerDistance() {
		return new JaroWinklerDistance();
	}
}
