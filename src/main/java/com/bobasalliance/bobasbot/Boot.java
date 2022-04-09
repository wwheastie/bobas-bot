package com.bobasalliance.bobasbot;

import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import com.bobasalliance.bobasbot.discord.connector.DiscordConnector;
import com.bobasalliance.bobasbot.discord.registrants.DiscordCommandRegistrant;

@SpringBootApplication
public class Boot implements CommandLineRunner {
	private final DiscordCommandRegistrant discordCommandRegistrant;
	private final DiscordConnector discordConnector;

	public Boot(final DiscordCommandRegistrant discordCommandRegistrant, final DiscordConnector discordConnector) {
		this.discordCommandRegistrant = discordCommandRegistrant;
		this.discordConnector = discordConnector;
	}

	public static void main(final String[] args) {
		new SpringApplicationBuilder(Boot.class)
				.web(WebApplicationType.NONE)
				.bannerMode(Banner.Mode.OFF)
				.logStartupInfo(false)
				.run(args);
	}

	@Override
	public void run(final String... args) throws Exception {
		discordCommandRegistrant.registerSlashCommands();
		discordConnector.connect();
	}
}
