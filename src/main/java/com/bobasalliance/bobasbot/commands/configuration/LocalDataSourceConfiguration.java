package com.bobasalliance.bobasbot.commands.configuration;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.netflix.archaius.api.Property;
import com.netflix.archaius.api.PropertyRepository;

@Component
// TODO: Create production version of this
public class LocalDataSourceConfiguration implements DataSourceConfiguration {
	private static final String DB_ADDRESS_KEY = "dbAddress";
	private static final String DB_PORT_KEY = "dbPort";
	private static final String DB_NAME_KEY = "dbName";
	private static final String DB_USERNAME_KEY = "dbUsername";
	private static final String DB_PASSWORD_KEY = "dbPassword";

	private static final String DB_ADDRESS_DEFAULT = "localhost";
	private static final String DB_PORT_DEFAULT = "3306";
	private static final String DB_NAME_DEFAULT = "jedistar";
	private static final String DB_USERNAME_DEFAULT = "root";
	private static final String DB_PASSWORD_DEFAULT = "root";

	private static final String JDBC_URL_FORMAT = "jdbc:mysql://%s:%s/%s";

	private final Property<String> dbAddress;
	private final Property<String> dbPort;
	private final Property<String> dbName;
	private final Property<String> dbUsername;
	private final Property<String> dbPassword;

	private final String jdbcUrl;

	@Inject
	public LocalDataSourceConfiguration(final PropertyRepository properties) {
		dbAddress = properties.get(DB_ADDRESS_KEY, String.class).orElse(DB_ADDRESS_DEFAULT);
		dbPort = properties.get(DB_PORT_KEY, String.class).orElse(DB_PORT_DEFAULT);
		dbName = properties.get(DB_NAME_KEY, String.class).orElse(DB_NAME_DEFAULT);
		dbUsername = properties.get(DB_USERNAME_KEY, String.class).orElse(DB_USERNAME_DEFAULT);
		dbPassword = properties.get(DB_PASSWORD_KEY, String.class).orElse(DB_PASSWORD_DEFAULT);
		jdbcUrl = String.format(JDBC_URL_FORMAT, dbAddress.get(), dbPort.get(), dbName.get());
	}

	@Override
	public String getUrl() {
		return jdbcUrl;
	}

	@Override
	public String getUsername() {
		return dbUsername.get();
	}

	@Override
	public String getPassword() {
		return dbPassword.get();
	}
}
