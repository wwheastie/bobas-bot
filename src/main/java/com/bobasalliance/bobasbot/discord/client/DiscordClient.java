package com.bobasalliance.bobasbot.discord.client;

import javax.ws.rs.core.Response;

import com.bobasalliance.bobasbot.discord.beans.DiscordCommandMetadata;

public interface DiscordClient {
	Response registerGuildSlashCommand(DiscordCommandMetadata discordCommand);

	Response registerGlobalSlashCommand(DiscordCommandMetadata discordCommandMetadata);
}
