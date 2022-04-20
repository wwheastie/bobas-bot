package com.bobasalliance.bobasbot.commands.factory;

import java.awt.Color;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.springframework.stereotype.Component;

import com.bobasalliance.bobasbot.commands.enums.MessageType;

@Component
public class DiscordEmbedMessageBuilderFactory {
	private static final Color SUCCESS_COLOR = Color.GREEN;
	private static final Color FAILURE_COLOR = Color.RED;
	private static final Color WARNING_COLOR = Color.YELLOW;

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
		return new EmbedBuilder().setColor(SUCCESS_COLOR);
	}

	private EmbedBuilder failureEmbedBuilder() {
		return new EmbedBuilder().setColor(FAILURE_COLOR);
	}

	private EmbedBuilder warningEmbedBuilder() {
		return new EmbedBuilder().setColor(WARNING_COLOR);
	}

	private EmbedBuilder defaultEmbedBuilder() {
		return new EmbedBuilder();
	}
}
