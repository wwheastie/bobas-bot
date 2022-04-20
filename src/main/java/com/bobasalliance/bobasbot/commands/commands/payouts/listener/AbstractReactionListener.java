package com.bobasalliance.bobasbot.commands.commands.payouts.listener;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.reaction.ReactionAddEvent;
import org.javacord.api.listener.message.reaction.ReactionAddListener;

import com.vdurmont.emoji.Emoji;

public abstract class AbstractReactionListener implements ReactionAddListener {
	protected final Logger logger = LogManager.getLogger(this.getClass());
	private static final String CANCEL_MESSAGE = "OK, cancelling.";

	private final Emoji emojiOK;
	private final Emoji emojiCancel;
	private final User user;

	public AbstractReactionListener(final Emoji emojiOK, final Emoji emojiCancel, final User user) {
		this.emojiOK = emojiOK;
		this.emojiCancel = emojiCancel;
		this.user = user;
	}

	@Override
	public void onReactionAdd(final ReactionAddEvent event) {
		CompletableFuture<User> userCompletableFuture = event.requestUser();
		try {
			User eventUser = userCompletableFuture.get(30, TimeUnit.SECONDS);//because it might not be cached
			if (eventUser.isYourself()
					|| !(eventUser.getId() == this.user.getId())) {
				return;
			}
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			logger.error("Could not get event user in time, so ignoring reaction %s", event, e);
			return;
		}

		if (emojiCancel.getUnicode().equals(event.getEmoji().asUnicodeEmoji().orElse(null))) {
			event.getChannel().sendMessage(CANCEL_MESSAGE);
			event.getMessage().ifPresent(message -> message.removeMessageAttachableListener(this));
			return;
		}

		if (!emojiOK.getUnicode().equals(event.getEmoji().asUnicodeEmoji().orElse(null))) {
			return;
		}

		Optional<String> userMessage = doReaction(event);

		userMessage.ifPresent(messageString -> event.getChannel().sendMessage(messageString));

		event.getMessage().ifPresent(message -> message.removeMessageAttachableListener(this));
	}

	protected abstract Optional<String> doReaction(final ReactionAddEvent event);
}
