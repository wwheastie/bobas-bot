package com.bobasalliance.bobasbot.discord.connector;

import org.javacord.api.DiscordApiBuilder;
import org.springframework.stereotype.Component;

import com.bobasalliance.bobasbot.discord.listener.DiscordSlashCommandEventListener;
import com.bobasalliance.bobasbot.discord.properties.DiscordProperties;

@Component
public class DiscordConnectorBase implements DiscordConnector {
	private final DiscordProperties discordProperties;
	private final DiscordSlashCommandEventListener discordSlashCommandEventListener;

	public DiscordConnectorBase(final DiscordProperties discordProperties,
			final DiscordSlashCommandEventListener discordSlashCommandEventListener) {
		this.discordProperties = discordProperties;
		this.discordSlashCommandEventListener = discordSlashCommandEventListener;
	}

	@Override
	public void connect() {
		new DiscordApiBuilder().setToken(discordProperties.getToken())
				.setRecommendedTotalShards()
				.join()
				.addSlashCommandCreateListener(discordSlashCommandEventListener)
				.loginAllShards();
	}
}
