package com.bobasalliance.bobasbot.discord.beans;

import java.util.List;

import com.bobasalliance.bobasbot.commands.beans.CommandOptionChoiceMetadata;

public class DiscordCommandOptionMetadata {
	private String name;
	private String description;
	private int type;
	private Boolean required;
	private List<DiscordCommandOptionMetadata> options;
	private List<CommandOptionChoiceMetadata> choices;

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public int getType() {
		return type;
	}

	public void setType(final int type) {
		this.type = type;
	}

	public Boolean getRequired() {
		return required;
	}

	public void setRequired(final Boolean required) {
		this.required = required;
	}

	public List<DiscordCommandOptionMetadata> getOptions() {
		return options;
	}

	public void setOptions(final List<DiscordCommandOptionMetadata> options) {
		this.options = options;
	}

	public List<CommandOptionChoiceMetadata> getChoices() {
		return choices;
	}

	public void setChoices(final List<CommandOptionChoiceMetadata> choices) {
		this.choices = choices;
	}
}
