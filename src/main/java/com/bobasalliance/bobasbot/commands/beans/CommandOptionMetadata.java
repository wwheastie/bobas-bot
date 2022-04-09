package com.bobasalliance.bobasbot.commands.beans;

import java.util.List;

import com.bobasalliance.bobasbot.commands.enums.FieldType;

public class CommandOptionMetadata {
	private final String name;
	private final String description;
	private final FieldType fieldType;
	private final boolean required;
	private final List<CommandOptionChoiceMetadata> choices;

	public CommandOptionMetadata(final Builder builder) {
		this.name = builder.name;
		this.description = builder.description;
		this.fieldType = builder.fieldType;
		this.required = builder.required;
		this.choices = builder.choices;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public FieldType getFieldType() {
		return fieldType;
	}

	public boolean isRequired() {
		return required;
	}

	public List<CommandOptionChoiceMetadata> getChoices() {
		return choices;
	}

	public static class Builder {
		private String name;
		private String description;
		private FieldType fieldType;
		private boolean required;
		private List<CommandOptionChoiceMetadata> choices;

		public Builder(final String name, final String description) {
			this.name = name;
			this.description = description;
		}

		public Builder fieldType(final FieldType fieldType) {
			this.fieldType = fieldType;
			return this;
		}

		public Builder required(final boolean required) {
			this.required = required;
			return this;
		}

		public Builder choices(final List<CommandOptionChoiceMetadata> choices) {
			this.choices = choices;
			return this;
		}

		public CommandOptionMetadata build() {
			return new CommandOptionMetadata(this);
		}
	}
}
