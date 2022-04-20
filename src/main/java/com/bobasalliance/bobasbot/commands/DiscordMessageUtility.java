package com.bobasalliance.bobasbot.commands;

import org.javacord.api.entity.message.embed.EmbedBuilder;

public class DiscordMessageUtility {
	private static final String SEPARATOR = "-";
	private static final String FOOTER_MESSAGE = "\n\nThis Bot is brought to you by [Boba’s Alliance](https://www.bobasalliance.com/)\r\nFind out more about Boba’s CR-Bot here : [https://discord.gg/p9zuj4W](https://discord.gg/p9zuj4W)";

	public static EmbedBuilder addFooter(final EmbedBuilder embedBuilder) {
		return embedBuilder.addField(SEPARATOR, FOOTER_MESSAGE);
	}

	private DiscordMessageUtility() {}
}
