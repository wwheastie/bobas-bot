package com.bobasalliance.bobasbot.commands.beans;

public class CommandOptionChoiceMetadata {
	private final String name;
	private final String value;

	public CommandOptionChoiceMetadata(final Builder builder) {
		this.name = builder.name;
		this.value = builder.value;
	}

	public static class Builder {
		private String name;
		private String value;

		public Builder(final String name, final String value) {
			this.name = name;
			this.value = value;
		}

		public CommandOptionChoiceMetadata build() {
			return new CommandOptionChoiceMetadata(this);
		}
	}
}
