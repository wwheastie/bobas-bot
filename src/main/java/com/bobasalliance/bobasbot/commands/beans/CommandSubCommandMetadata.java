package com.bobasalliance.bobasbot.commands.beans;

import java.util.List;

public class CommandSubCommandMetadata {
	private final String name;
	private final String description;
	private final List<CommandOptionMetadata> options;

	public CommandSubCommandMetadata(final Builder builder) {
		this.name = builder.name;
		this.description = builder.description;
		this.options = builder.options;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public List<CommandOptionMetadata> getOptions() {
		return options;
	}

	public static class Builder {
		private String name;
		private String description;
		private List<CommandOptionMetadata> options;

		public Builder(final String name, final String description) {
			this.name = name;
			this.description = description;
		}

		public Builder options(final List<CommandOptionMetadata> options) {
			this.options = options;
			return this;
		}

		public CommandSubCommandMetadata build() {
			return new CommandSubCommandMetadata(this);
		}
	}
}
