package com.bobasalliance.bobasbot.commands.beans;

import java.util.Map;

import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;

public class EventDetails {
	private final User user;
	private final Server server;
	private final String channelId;
	private final String subCommandName;
	private final Map<String, String> options;

	public EventDetails(final Builder builder) {
		this.user = builder.user;
		this.server = builder.server;
		this.channelId = builder.channelId;
		this.subCommandName = builder.subCommandName;
		this.options = builder.options;
	}

	public User getUser() {
		return user;
	}

	public Server getServer() {
		return server;
	}

	public String getChannelId() {
		return channelId;
	}

	public String getSubCommandName() {
		return subCommandName;
	}

	public String getOption(final String option) {
		return options.get(option);
	}

	public static class Builder {
		private User user;
		private Server server;
		private String channelId;
		private String subCommandName;
		private Map<String, String> options;

		public Builder user(final User user) {
			this.user = user;
			return this;
		}

		public Builder server(final Server server) {
			this.server = server;
			return this;
		}

		public Builder channelId(final String channelId) {
			this.channelId = channelId;
			return this;
		}

		public Builder subCommandName(final String subCommandName) {
			this.subCommandName = subCommandName;
			return this;
		}

		public Builder options(final Map<String, String> options) {
			this.options = options;
			return this;
		}

		public EventDetails build() {
			return new EventDetails(this);
		}
	}
}
