package com.bobasalliance.bobasbot.discord.connector;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.util.logging.ExceptionLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.bobasalliance.bobasbot.discord.configuration.Settings;
import com.bobasalliance.bobasbot.discord.listener.DiscordSlashCommandEventListener;

@Component
public class DiscordConnectorBase implements DiscordConnector {
	private static final Logger LOG = LoggerFactory.getLogger(DiscordConnectorBase.class);
	private static int counter = 0;

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
				.loginAllShards()
				.forEach(shardFuture -> shardFuture
						.thenAcceptAsync(this::onShardLogin)
						.exceptionally(ExceptionLogger.get()));
	}

	private void onShardLogin(final DiscordApi discordApi) {
		LOG.info("bobasbot - {} shard has been logged in...", ++counter);
		discordApi.addSlashCommandCreateListener(discordSlashCommandEventListener);
	}
}
