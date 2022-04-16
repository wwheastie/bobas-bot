package com.bobasalliance.bobasbot.commands.api.payouts.sub.commands;

import java.util.Arrays;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.springframework.stereotype.Component;

import com.bobasalliance.bobasbot.commands.AdminUtility;
import com.bobasalliance.bobasbot.commands.api.ReactionEmojis;
import com.bobasalliance.bobasbot.commands.api.payouts.PayoutTimeRepository;
import com.bobasalliance.bobasbot.commands.api.payouts.PayoutsCommandMetadataUtility;
import com.bobasalliance.bobasbot.commands.api.payouts.listener.PayoutDeleteReactionListener;
import com.bobasalliance.bobasbot.commands.beans.CommandAnswer;
import com.bobasalliance.bobasbot.commands.beans.EventDetails;
import com.bobasalliance.bobasbot.commands.enums.MessageType;
import com.bobasalliance.bobasbot.commands.factory.DiscordEmbedMessageBuilderFactory;
import com.vdurmont.emoji.Emoji;

@Component
public class PayoutsDeleteSubCommand implements PayoutsSubCommand {
	private static final String PAYOUT_DELETE_TITLE = "Delete User from Payouts List";
	private static final String MESSAGE_CONFIRM_DELETE = "Are you sure you want to remove **%s** ? **'Check' to remove, 'X' to keep.**";
	private static final String NO_USERS_IN_CHANNEL_ERROR_MESSAGE = "%s does not exist in channel";
	private static final String NON_ADMIN_USER_MESSAGE = "You may not execute this command.\nYou must be part of the «botAdmins» Discord group to administrate the bot.";

	private final PayoutTimeRepository payoutTimeRepository;
	private final DiscordEmbedMessageBuilderFactory embedMessageBuilderFactory;

	public PayoutsDeleteSubCommand(final PayoutTimeRepository payoutTimeRepository, final DiscordEmbedMessageBuilderFactory embedMessageBuilderFactory) {
		this.payoutTimeRepository = payoutTimeRepository;
		this.embedMessageBuilderFactory = embedMessageBuilderFactory;
	}

	@Override
	public String getName() {
		return PayoutsCommandMetadataUtility.DELETE_SUB_COMMAND_NAME;
	}

	@Override
	public CommandAnswer execute(final EventDetails eventDetails) {
		if (isAdmin(eventDetails)) {
			final String channelId = eventDetails.getChannelId();
			final String userName = eventDetails.getOption(PayoutsCommandMetadataUtility.USER_NAME_COMMAND_OPTION);

			if (userDoesNotExist(channelId, userName)) {
				return userDoesNotExistCommandAnswer(userName);
			}

			return deleteUserConfirmationCommandAnswer(eventDetails, userName);
		}

		return isNotAdminCommandAnswer();
	}

	private boolean isAdmin(final EventDetails eventDetails) {
		return AdminUtility.isAdmin(eventDetails.getUser(), eventDetails.getServer());
	}

	private boolean userDoesNotExist(final String channelId, final String userName) {
		return !payoutTimeRepository.findByChannelIdAndUserName(channelId, userName).isPresent();
	}

	private CommandAnswer userDoesNotExistCommandAnswer(final String userName) {
		EmbedBuilder embedMessage = embedMessageBuilderFactory.getEmbedMessageBuilder(MessageType.WARNING)
				.setTitle(PAYOUT_DELETE_TITLE)
				.addField("", String.format(NO_USERS_IN_CHANNEL_ERROR_MESSAGE, userName), false);
		return new CommandAnswer.Builder().embedMessages(Arrays.asList(embedMessage)).build();
	}

	private CommandAnswer deleteUserConfirmationCommandAnswer(final EventDetails eventDetails, final String userName) {
		final Emoji okEmoji = ReactionEmojis.getOk();
		final Emoji cancelEmoji = ReactionEmojis.getCancel();

		PayoutDeleteReactionListener payoutDeleteReactionListener = new PayoutDeleteReactionListener(okEmoji,
				cancelEmoji, payoutTimeRepository, eventDetails);

		return new CommandAnswer.Builder()
				.message(String.format(MESSAGE_CONFIRM_DELETE, userName))
				.reactionEmojis(Arrays.asList(okEmoji, cancelEmoji))
				.reactionListener(payoutDeleteReactionListener)
				.build();
	}

	private CommandAnswer isNotAdminCommandAnswer() {
		return new CommandAnswer.Builder().message(NON_ADMIN_USER_MESSAGE).build();
	}
}
