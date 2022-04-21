package com.bobasalliance.bobasbot.discord.configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections4.set.UnmodifiableSet;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class Settings {
	private static final String JSON_SETTINGS_FILE_NAME = "settings.json";
	private static final String UTF_8_CHARSET = "utf-8";

	private final JSONObject settingsJsonObject;
	private final Set<String> botAdminsGroups;
	private final Set<Integer> botAdminsUsers;

	public Settings() throws IOException {
		final byte[] settingsJsonBytes = Files.readAllBytes(Paths.get(JSON_SETTINGS_FILE_NAME));
		final String settingsJsonString = new String(settingsJsonBytes, UTF_8_CHARSET);
		settingsJsonObject = new JSONObject(settingsJsonString);
		botAdminsGroups = setBotAdminsGroups();
		botAdminsUsers = setBotAdminsUsers();
	}

	private Set<String> setBotAdminsGroups() {
		JSONArray jsonArray = settingsJsonObject.getJSONObject("botAdmins").getJSONArray("groups");
		Set<String> set = new HashSet<>(jsonArray.length());
		for (int i = 0; i < jsonArray.length(); i++) {
			set.add(jsonArray.getString(i));
		}
		return UnmodifiableSet.unmodifiableSet(set);
	}

	private Set<Integer> setBotAdminsUsers() {
		JSONArray jsonArray = settingsJsonObject.getJSONObject("botAdmins").getJSONArray("users");
		Set<Integer> set = new HashSet<>(jsonArray.length());
		for (int i = 0; i < jsonArray.length(); i++) {
			set.add(jsonArray.getInt(i));
		}
		return UnmodifiableSet.unmodifiableSet(set);
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
		return settingsJsonObject.getJSONObject("database").getString("pwd");
	}

	public String getApplicationId() {
		return settingsJsonObject.getString("applicationId");
	}

	public String getGuildId() {
		return settingsJsonObject.getString("guildId");
	}

	public Set<String> getBotAdminsGroup() {
		return botAdminsGroups;
	}

	public Set<Integer> getBotAdminsUsers() {
		return botAdminsUsers;
	}
}
