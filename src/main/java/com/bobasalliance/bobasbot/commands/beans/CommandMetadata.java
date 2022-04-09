package com.bobasalliance.bobasbot.commands.beans;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

public class CommandMetadata {
	private final String name;
	private final String description;
	private final List<CommandOptionMetadata> options;
	private final List<CommandSubCommandMetadata> subCommands;
	private final boolean hasSubCommands;
	private final boolean hasOptions;

	private CommandMetadata(final Builder builder) {
		this.name = builder.name;
		this.description = builder.description;
		this.options = builder.options;
		this.subCommands = builder.subCommands;
		this.hasSubCommands = CollectionUtils.isNotEmpty(subCommands);
		this.hasOptions = CollectionUtils.isNotEmpty(options);
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

	public List<CommandSubCommandMetadata> getSubCommands() {
		return subCommands;
	}

	public boolean hasSubCommands() {
		return hasSubCommands;
	}

	public boolean hasOptions() {
		return hasOptions;
	}

	public static class Builder {
		private String name;
		private String description;
		private List<CommandOptionMetadata> options;
		private List<CommandSubCommandMetadata> subCommands;

		public Builder(final String name, final String description) {
			this.name = name;
			this.description = description;
		}

		public Builder options(final List<CommandOptionMetadata> options) {
			this.options = options;
			return this;
		}

		public Builder subCommand(final CommandSubCommandMetadata subCommandMetadata) {
			if (CollectionUtils.isEmpty(subCommands)) {
				subCommands = new ArrayList<>();
			}
			subCommands.add(subCommandMetadata);
			return this;
		}

		public CommandMetadata build() {
			return new CommandMetadata(this);
		}
	}
}
