package com.bobasalliance.bobasbot.discord.beans;

import java.util.List;

public class DiscordCommandMetadata {
	private String name;
	private String description;
	private List<DiscordCommandOptionMetadata> options;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<DiscordCommandOptionMetadata> getOptions() {
		return options;
	}

	public void setOptions(List<DiscordCommandOptionMetadata> options) {
		this.options = options;
	}
}
