package com.bobasalliance.bobasbot.discord.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.bobasalliance.bobasbot.discord.beans.DiscordCommandMetadata;
import com.bobasalliance.bobasbot.discord.configuration.Settings;

@Component
public class DiscordClientBase implements DiscordClient {
	private static final Logger LOG = LoggerFactory.getLogger(DiscordClientBase.class);
	private static final String DISCORD_COMMAND_REGISTER_API = "https://discord.com/api/v8/applications/{applicationId}/guilds/{guildId}/commands";
	private static final String DISCORD_GLOBAL_COMMAND_REGISTER_API = "https://discord.com/api/v8/applications/{applicationId}/commands";
	private static final String APPLICATION_ID_TEMPLATE = "applicationId";
	private static final String GUILD_ID_TEMPLATE = "guildId";
	private static final String AUTHORIZATION_STRING_FORMAT = "Bot %s";

	private final Client client;
	private final Settings settings;

	public DiscordClientBase(final Client client, final Settings settings) {
		this.client = client;
		this.settings = settings;
	}

	@Override
	public Response registerGuildSlashCommand(final DiscordCommandMetadata discordCommand) {
		Response response = client.target(DISCORD_COMMAND_REGISTER_API)
				.resolveTemplate(APPLICATION_ID_TEMPLATE, settings.getApplicationId())
				.resolveTemplate(GUILD_ID_TEMPLATE, settings.getGuildId())
				.request(MediaType.APPLICATION_JSON_TYPE)
				.header(HttpHeaders.AUTHORIZATION, getDiscordAuthorization())
				.post(Entity.json(discordCommand));

		LOG.info("Creating {} command returned {} status", discordCommand.getName(), response.getStatus());

		return response;
	}

	@Override
	public Response registerGlobalSlashCommand(final DiscordCommandMetadata discordCommand) {
		Response response = client.target(DISCORD_GLOBAL_COMMAND_REGISTER_API)
				.resolveTemplate(APPLICATION_ID_TEMPLATE, settings.getApplicationId())
				.request(MediaType.APPLICATION_JSON_TYPE)
				.header(HttpHeaders.AUTHORIZATION, getDiscordAuthorization())
				.post(Entity.json(discordCommand));

		LOG.info("Creating {} command returned {} status", discordCommand.getName(), response.getStatus());

		return response;
	}

	private String getDiscordAuthorization() {
		return String.format(AUTHORIZATION_STRING_FORMAT, settings.getDiscordToken());
	}
}
