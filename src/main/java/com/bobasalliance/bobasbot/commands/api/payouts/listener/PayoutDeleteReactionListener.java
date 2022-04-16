package com.bobasalliance.bobasbot.commands.api.payouts.listener;

import java.util.Optional;

import org.javacord.api.event.message.reaction.ReactionAddEvent;

import com.bobasalliance.bobasbot.commands.api.payouts.PayoutTimeRepository;
import com.bobasalliance.bobasbot.commands.api.payouts.PayoutsCommandMetadataUtility;
import com.bobasalliance.bobasbot.commands.beans.EventDetails;
import com.vdurmont.emoji.Emoji;

public class PayoutDeleteReactionListener extends AbstractReactionListener {
	private static final String USER_REMOVED_CONFIRMATION_MESSAGE = "%s has been removed";

	private final PayoutTimeRepository payoutTimeRepository;
	private final EventDetails eventDetails;

	public PayoutDeleteReactionListener(final Emoji emojiOK, final Emoji emojiCancel, final PayoutTimeRepository payoutTimeRepository,
			final EventDetails eventDetails) {
		super(emojiOK, emojiCancel, eventDetails.getUser());
		this.payoutTimeRepository = payoutTimeRepository;
		this.eventDetails = eventDetails;
	}

	@Override
	protected Optional<String> doReaction(final ReactionAddEvent event) {
		final String userName = eventDetails.getOption(PayoutsCommandMetadataUtility.USER_NAME_COMMAND_OPTION);
		payoutTimeRepository.deleteByChannelIdAndUserName(eventDetails.getChannelId(), userName);
		return Optional.of(String.format(USER_REMOVED_CONFIRMATION_MESSAGE, userName));
	}
}
