package com.bobasalliance.bobasbot.commands.beans;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.listener.message.reaction.ReactionAddListener;

import com.vdurmont.emoji.Emoji;

public class CommandAnswer {
	private final String message;
	private final List<EmbedBuilder> embedMessages;
	private final ReactionAddListener reactionListener;
	private final List<Emoji> reactionEmojis;
	private final boolean hasMessage;
	private final boolean hasEmbedMessages;
	private final boolean hasReactionListener;

	private CommandAnswer(final Builder builder) {
		this.message = builder.message;
		this.embedMessages = builder.embedMessages;
		this.reactionListener = builder.reactionListener;
		this.reactionEmojis = builder.reactionEmojis;
		this.hasMessage = StringUtils.isNotEmpty(message);
		this.hasEmbedMessages = CollectionUtils.isNotEmpty(embedMessages);
		this.hasReactionListener = reactionListener != null;
	}

	public String getMessage() {
		return message;
	}

	public List<EmbedBuilder> getEmbedMessages() {
		return embedMessages;
	}

	public ReactionAddListener getReactionListener() {
		return reactionListener;
	}

	public List<Emoji> getReactionEmojis() {
		return reactionEmojis;
	}

	public boolean hasMessage() {
		return hasMessage;
	}

	public boolean hasEmbedMessages() {
		return hasEmbedMessages;
	}

	public boolean hasReactionListener() {
		return hasReactionListener;
	}

	public static class Builder {
		private String message;
		private List<EmbedBuilder> embedMessages;
		private ReactionAddListener reactionListener;
		private List<Emoji> reactionEmojis;

		public Builder message(final String message) {
			this.message = message;
			return this;
		}

		public Builder embedMessages(final List<EmbedBuilder> embedMessages) {
			this.embedMessages = embedMessages;
			return this;
		}

		public Builder reactionListener(final ReactionAddListener reactionListener) {
			this.reactionListener = reactionListener;
			return this;
		}

		public Builder reactionEmojis(final List<Emoji> reactionEmojis) {
			this.reactionEmojis = reactionEmojis;
			return this;
		}

		public CommandAnswer build() {
			return new CommandAnswer(this);
		}
	}
}
