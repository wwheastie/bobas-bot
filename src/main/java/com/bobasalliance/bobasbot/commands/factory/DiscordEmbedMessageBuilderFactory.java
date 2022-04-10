package com.bobasalliance.bobasbot.commands.factory;

import java.awt.*;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.springframework.stereotype.Component;

import com.bobasalliance.bobasbot.commands.enums.MessageType;

@Component
public class DiscordEmbedMessageBuilderFactory {
	private static final Color SUCCESS_COLOR = Color.GREEN;
	private static final Color FAILURE_COLOR = Color.RED;
	private static final Color WARNING_COLOR = Color.YELLOW;
	private static final String FOOTER_MESSAGE = "\n-\nThis Bot is brought to you by [Boba’s Alliance](https://www.bobasalliance.com/)\r\nFind out more about Boba’s CR-Bot here : [https://discord.gg/p9zuj4W](https://discord.gg/p9zuj4W)";

	public EmbedBuilder getEmbedMessageBuilder(final MessageType type) {
		if (MessageType.SUCCESS.equals(type)) {
			return successEmbedBuilder();
		} else if (MessageType.FAILURE.equals(type)) {
			return failureEmbedBuilder();
		} else if (MessageType.WARNING.equals(type)) {
			return warningEmbedBuilder();
		} else {
			return defaultEmbedBuilder();
		}
	}

	private EmbedBuilder successEmbedBuilder() {
		return defaultEmbedBuilder().setColor(SUCCESS_COLOR);
	}

	private EmbedBuilder failureEmbedBuilder() {
		return defaultEmbedBuilder().setColor(FAILURE_COLOR);
	}

	private EmbedBuilder warningEmbedBuilder() {
		return defaultEmbedBuilder().setColor(WARNING_COLOR);
	}

	private EmbedBuilder defaultEmbedBuilder() {
		return new EmbedBuilder()
				.setFooter(FOOTER_MESSAGE);
	}
}
