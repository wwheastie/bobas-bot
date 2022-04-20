package com.bobasalliance.bobasbot.commands.commands.payouts.listener;

import java.sql.Time;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Optional;

import org.javacord.api.event.message.reaction.ReactionAddEvent;

import com.bobasalliance.bobasbot.commands.beans.EventDetails;
import com.bobasalliance.bobasbot.commands.commands.payouts.PayoutTimeRepository;
import com.bobasalliance.bobasbot.commands.commands.payouts.PayoutsCommandMetadataUtility;
import com.vdurmont.emoji.Emoji;

public class PayoutAddReactionListener extends AbstractReactionListener {
	private static final String SUCCESSFUL_USER_ADD_MESSAGE = "%s has been added to payouts list";

	private final GregorianCalendar payoutCalendar;
	private final PayoutTimeRepository payoutTimeRepository;
	private final EventDetails eventDetails;
	private final String swgohggLink;
	private final String flag;

	public PayoutAddReactionListener(final PayoutTimeRepository payoutTimeRepository, final GregorianCalendar payoutCalendar,
			final String swgohggLink, final String flag, final Emoji emojiOK, final Emoji emojiCancel, final EventDetails eventDetails) {
		super(emojiOK, emojiCancel, eventDetails.getUser());
		this.payoutCalendar = payoutCalendar;
		this.payoutTimeRepository = payoutTimeRepository;
		this.eventDetails = eventDetails;
		this.swgohggLink = swgohggLink;
		this.flag = flag;
	}

	@Override
	protected Optional<String> doReaction(final ReactionAddEvent event) {
		final String userName = eventDetails.getOption(PayoutsCommandMetadataUtility.USER_NAME_COMMAND_OPTION_NAME);
		long timeMillisUTC = (long) payoutCalendar.get(Calendar.HOUR_OF_DAY) * 60 * 60 * 1000;
		timeMillisUTC = timeMillisUTC + (long) payoutCalendar.get(Calendar.MINUTE) * 60 * 1000;
		timeMillisUTC = timeMillisUTC - (long) payoutCalendar.getTimeZone().getOffset(System.currentTimeMillis());
		payoutTimeRepository.savePayoutTime(eventDetails.getChannelId(), userName,
				new Time(timeMillisUTC), flag, swgohggLink);
		return Optional.of(String.format(SUCCESSFUL_USER_ADD_MESSAGE, userName));
	}
}
