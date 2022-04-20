package com.bobasalliance.bobasbot.discord.configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class Settings {
	private static final String JSON_SETTINGS_FILE_NAME = "settings.json";
	private static final String UTF_8_CHARSET = "utf-8";

	private final JSONObject settingsJsonObject;

	public Settings() throws IOException {
		final byte[] settingsJsonBytes = Files.readAllBytes(Paths.get(JSON_SETTINGS_FILE_NAME));
		final String settingsJsonString = new String(settingsJsonBytes, UTF_8_CHARSET);
		settingsJsonObject = new JSONObject(settingsJsonString);
	}

	public String getDiscordToken() {
		return settingsJsonObject.getString("discordToken");
	}

	public String getDatabaseUrl() {
		return settingsJsonObject.getJSONObject("database").getString("url");
	}

	public String getDatabaseUser() {
		return settingsJsonObject.getJSONObject("database").getString("user");
	}

	public String getDatabasePassword() {
		return settingsJsonObject.getJSONObject("database").getString("password");
	}

	public String getApplicationId() {
		return settingsJsonObject.getString("applicationId");
	}

	public String getGuildId() {
		return settingsJsonObject.getString("guildId");
	}
}
