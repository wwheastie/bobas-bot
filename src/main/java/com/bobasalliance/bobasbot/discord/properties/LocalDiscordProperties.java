package com.bobasalliance.bobasbot.discord.properties;

import org.springframework.stereotype.Component;

import com.netflix.archaius.api.PropertyRepository;

@Component
public class LocalDiscordProperties implements DiscordProperties {
	private static final String DISCORD_TOKEN_KEY = "discordToken";
	private static final String DEFAULT_DISCORD_TOKEN_VALUE = "ODg2MDg2NzgzNTcyNTA4Njky.YTwenw.WRvd-yhxDIr7p9b21NiUE1-QmRU";

	private static final String APPLICATION_ID_KEY = "applicationId";
	private static final String DEFAULT_APPLICATION_ID_VALUE = "886086783572508692";

	private static final String GUILD_ID_KEY = "guildId";
	private static final String DEFAULT_GUILD_VALUE = "668201955864084501";

	private final String token;
	private final String applicationId;
	private final String guildId;

	public LocalDiscordProperties(final PropertyRepository properties) {
		this.token = properties.get(DISCORD_TOKEN_KEY, String.class).orElse(DEFAULT_DISCORD_TOKEN_VALUE).get();
		this.applicationId = properties.get(APPLICATION_ID_KEY, String.class).orElse(DEFAULT_APPLICATION_ID_VALUE).get();
		this.guildId = properties.get(GUILD_ID_KEY, String.class).orElse(DEFAULT_GUILD_VALUE).get();
	}

	@Override
	public String getToken() {
		return token;
	}

	@Override
	public String getApplicationId() {
		return applicationId;
	}

	@Override
	public String getGuildId() {
		return guildId;
	}
}
