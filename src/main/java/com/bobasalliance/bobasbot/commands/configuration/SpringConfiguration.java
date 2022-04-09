package com.bobasalliance.bobasbot.commands.configuration;

import javax.sql.DataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfiguration {
	@Bean
	public DataSource getDataSource(final DataSourceConfiguration dataSourceConfiguration) {
		return DataSourceBuilder.create()
				.url(dataSourceConfiguration.getUrl())
				.username(dataSourceConfiguration.getUsername())
				.password(dataSourceConfiguration.getPassword())
				.build();
	}
}
