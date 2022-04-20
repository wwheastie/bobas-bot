package com.bobasalliance.bobasbot.discord.connector;

import org.javacord.api.DiscordApiBuilder;
import org.springframework.stereotype.Component;

import com.bobasalliance.bobasbot.discord.configuration.Settings;
import com.bobasalliance.bobasbot.discord.listener.DiscordSlashCommandEventListener;

@Component
public class DiscordConnectorBase implements DiscordConnector {
	private final Settings settings;
	private final DiscordSlashCommandEventListener discordSlashCommandEventListener;

	public DiscordConnectorBase(final Settings settings,
			final DiscordSlashCommandEventListener discordSlashCommandEventListener) {
		this.settings = settings;
		this.discordSlashCommandEventListener = discordSlashCommandEventListener;
	}

	@Override
	public void connect() {
		new DiscordApiBuilder().setToken(settings.getDiscordToken())
				.setRecommendedTotalShards()
				.join()
				.addSlashCommandCreateListener(discordSlashCommandEventListener)
				.loginAllShards();
	}
}
