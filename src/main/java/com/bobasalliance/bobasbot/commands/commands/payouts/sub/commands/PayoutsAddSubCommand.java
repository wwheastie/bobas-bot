package com.bobasalliance.bobasbot.commands.commands.payouts.sub.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.lang3.StringUtils;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.listener.message.reaction.ReactionAddListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.bobasalliance.bobasbot.commands.AdminUtility;
import com.bobasalliance.bobasbot.commands.beans.CommandAnswer;
import com.bobasalliance.bobasbot.commands.beans.EventDetails;
import com.bobasalliance.bobasbot.commands.commands.payouts.PayoutTimeRepository;
import com.bobasalliance.bobasbot.commands.commands.payouts.PayoutsCommandMetadataUtility;
import com.bobasalliance.bobasbot.commands.commands.payouts.listener.PayoutAddReactionListener;
import com.bobasalliance.bobasbot.commands.enums.MessageType;
import com.bobasalliance.bobasbot.commands.factory.DiscordEmbedMessageBuilderFactory;
import com.vdurmont.emoji.Emoji;
import com.vdurmont.emoji.EmojiManager;

@Component
public class PayoutsAddSubCommand implements PayoutsSubCommand {
	private static final Logger LOG = LoggerFactory.getLogger(PayoutsAddSubCommand.class);

	private static final String DEFAULT_FLAG = ":flag_white:";
	private static final String MESSAGE_CONFIRM_TIMEZONE = "Using timezone **%s**, my guess is **%s**'s payout is **%02d:%02d** hours from now.\nIs it correct ?";
	private static final String HELP = "This command allows you to manage your arena payouts.\n**Examples :** %payouts, %p\n%payouts list\n%payouts all\n%payouts 4\n**Administration commands :**\n%payouts add user 15:00 UTC flag_us https://swgoh.gg/u/user\n%payouts add user 12:00 EST\n%payouts delete user\n%payouts cleanup\n%payouts clear\n%payouts copy #channel\n%payouts defaultLimit 3";
	private static final String ERROR_MESSAGE = "Thanks for using my services. Unfortunately I can't answer for the following reason :\n";
	private static final String ERROR_TIME_FORMAT = "The time format is incorrect. Please use HH:MM format (for instance 08:30)";
	private static final String NO_TIME_ZONE_FOUND = "No timezone found. Here is a file containing a list of all possible timezones.";
	private static final String MESSAGE_TIMEZONE_CHOICE = "The following timezones match your input :";

	private final PayoutTimeRepository payoutTimeRepository;
	private final DiscordEmbedMessageBuilderFactory discordEmbedMessageBuilderFactory;

	public PayoutsAddSubCommand(final PayoutTimeRepository payoutTimeRepository, final DiscordEmbedMessageBuilderFactory discordEmbedMessageBuilderFactory) {
		this.payoutTimeRepository = payoutTimeRepository;
		this.discordEmbedMessageBuilderFactory = discordEmbedMessageBuilderFactory;
	}

	@Override
	public String getName() {
		return PayoutsCommandMetadataUtility.ADD_SUB_COMMAND_NAME;
	}

	@Override
	public CommandAnswer execute(final EventDetails eventDetails) {
		if (isAdmin(eventDetails)) {
			return addUserConfirmationCommandAnswer(eventDetails);
		}

		return isNotAdminCommandAnswer();
	}

	private boolean isAdmin(final EventDetails eventDetails) {
		return AdminUtility.isAdmin(eventDetails.getUser(), eventDetails.getServer());
	}

	private CommandAnswer addUserConfirmationCommandAnswer(final EventDetails eventDetails) {
		String userName = eventDetails.getOption(PayoutsCommandMetadataUtility.USER_NAME_COMMAND_OPTION_NAME);
		String payoutTime = eventDetails.getOption(PayoutsCommandMetadataUtility.PAYOUT_TIME_COMMAND_OPTION_NAME);
		String payoutTimeZone = eventDetails.getOption(PayoutsCommandMetadataUtility.PAYOUT_TIME_ZONE_COMMAND_OPTION_NAME);
		Optional<String> exactTimeZone = getExactTimeZone(payoutTimeZone);
		List<String> approximateTimeZones = getApproximateTimeZones(exactTimeZone, payoutTimeZone);
		String flag = getFlag(eventDetails);
		String swgohggLink = StringUtils.isNotEmpty(eventDetails.getOption(PayoutsCommandMetadataUtility.SWGOHGG_LINK_COMMAND_OPTION_NAME))
				? eventDetails.getOption(PayoutsCommandMetadataUtility.SWGOHGG_LINK_COMMAND_OPTION_NAME)
				: StringUtils.EMPTY;

		if (isNotValidTimeFormat(payoutTime)) {
			return errorCommandAnswer(ERROR_TIME_FORMAT);
		}

		if (exactTimeZone.isPresent() || approximateTimeZones.size() == 1) {
			final String timezone = exactTimeZone.orElseGet(() -> IterableUtils.first(approximateTimeZones));
			return confirmTimezone(userName, payoutTime, timezone, flag, swgohggLink, eventDetails);
		} else if (CollectionUtils.isNotEmpty(approximateTimeZones)) {
			return approximateTimeZoneCommandAnswer(approximateTimeZones);
		} else {
			return multipleTimeZones();
		}
	}

	private String getFlag(final EventDetails eventDetails) {
		final String flag = StringUtils.isNotEmpty(eventDetails.getOption(PayoutsCommandMetadataUtility.COUNTRY_FLAG_COMMAND_OPTION_NAME))
				? eventDetails.getOption(PayoutsCommandMetadataUtility.COUNTRY_FLAG_COMMAND_OPTION_NAME)
				: DEFAULT_FLAG;

		return StringUtils.appendIfMissing(StringUtils.prependIfMissing(flag, ":"), ":");
	}

	private Optional<String> getExactTimeZone(final String payoutTimeZone) {
		return Arrays.stream(TimeZone.getAvailableIDs()).filter(payoutTimeZone::equalsIgnoreCase).findFirst();
	}

	private List<String> getApproximateTimeZones(final Optional<String> exactTimeZone, final String payoutTimeZone) {
		if (exactTimeZone.isEmpty()) {
			return Arrays.stream(TimeZone.getAvailableIDs())
					.filter(timezone -> StringUtils.containsIgnoreCase(timezone, payoutTimeZone))
					.collect(Collectors.toList());
		}

		return Collections.emptyList();
	}

	private boolean isNotValidTimeFormat(final String payoutTime) {
		Pattern pattern = Pattern.compile("[0-9]{2}:[0-9]{2}", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(payoutTime);
		return !matcher.matches();
	}

	private CommandAnswer errorCommandAnswer(final String message) {
		return new CommandAnswer.Builder().message(ERROR_MESSAGE + "**" + message + "**\r\n\r\n" + HELP).build();
	}

	private CommandAnswer confirmTimezone(final String userName, final String payoutTime, final String timezoneName,
			final String flag, final String swgohggLink, final EventDetails eventDetails) {
		Emoji emojiX = EmojiManager.getForAlias("x");
		Emoji emojiV = EmojiManager.getForAlias("white_check_mark");

		TimeZone timezone = TimeZone.getTimeZone(timezoneName);

		Calendar now = getCalendarWithoutDate(timezone);

		Calendar payoutCalendar = getPayoutTime(payoutTime, timezone);

		GregorianCalendar payoutCalendar2 = new GregorianCalendar(payoutCalendar.getTimeZone());
		payoutCalendar2.setTimeInMillis(payoutCalendar.getTimeInMillis());

		if (payoutCalendar.before(now)) {
			payoutCalendar.add(Calendar.DAY_OF_MONTH, 1);

		}
		Long difference = payoutCalendar.getTimeInMillis() - now.getTimeInMillis();

		Long hoursDifference = difference / (60 * 60 * 1000) % 24;
		Long minutesDifference = difference / (60 * 1000) % 60;

		String message = String.format(MESSAGE_CONFIRM_TIMEZONE, timezoneName, userName, hoursDifference, minutesDifference);

		PayoutAddReactionListener listener = new PayoutAddReactionListener(payoutTimeRepository, payoutCalendar2, swgohggLink, flag, emojiV, emojiX,
				eventDetails);

		return addCommandAnswer(message, Arrays.asList(emojiV, emojiX), listener);
	}

	private GregorianCalendar getCalendarWithoutDate(TimeZone tz) {

		GregorianCalendar calendar = new GregorianCalendar(tz);

		Integer hours = calendar.get(Calendar.HOUR_OF_DAY);
		Integer minutes = calendar.get(Calendar.MINUTE);

		calendar.setTimeInMillis(0);

		calendar.set(Calendar.HOUR_OF_DAY, hours);
		calendar.set(Calendar.MINUTE, minutes);

		calendar.getTimeInMillis();
		return calendar;
	}

	private Calendar getPayoutTime(String string, TimeZone timezone) {

		Pattern pattern = Pattern.compile("[0-9]{2}:[0-9]{2}");
		Matcher matcher = pattern.matcher(string);

		if (!matcher.matches()) {
			return null;
		}

		String[] split = string.split(":");

		Integer hours = Integer.parseInt(split[0]);
		Integer minutes = Integer.parseInt(split[1]);

		Calendar calendar = Calendar.getInstance(timezone);

		calendar.setTimeInMillis(0);

		calendar.setTimeZone(timezone);

		calendar.set(Calendar.HOUR_OF_DAY, hours);
		calendar.set(Calendar.MINUTE, minutes);

		calendar.getTimeInMillis();

		return calendar;
	}

	private CommandAnswer approximateTimeZoneCommandAnswer(final List<String> approximateTimeZones) {
		EmbedBuilder embed = discordEmbedMessageBuilderFactory.getEmbedMessageBuilder(MessageType.WARNING);
		embed.setTitle(MESSAGE_TIMEZONE_CHOICE);

		int counter = 10;
		String content = "";
		for (String timeZone : approximateTimeZones) {
			content += timeZone + "\r\n";
			counter--;

			if (counter == 0) {
				embed.addField(".", content, true);
				content = "";
				counter = 10;
			}
		}

		if (StringUtils.isNotBlank(content)) {
			embed.addField(".", content, true);

		}

		return new CommandAnswer.Builder()
				.embedMessages(Arrays.asList(embed))
				.build();
	}

	private CommandAnswer addCommandAnswer(final String message, final List<Emoji> reactionEmojis, final ReactionAddListener reactionAddListener) {
		return new CommandAnswer.Builder()
				.message(message)
				.reactionEmojis(reactionEmojis)
				.reactionListener(reactionAddListener)
				.build();
	}

	private CommandAnswer multipleTimeZones() {
		Path path = Paths.get("timezones.txt");

		String timezones = "";
		for (String tz : TimeZone.getAvailableIDs()) {
			timezones += tz + "\r\n";
		}

		try {
			Files.write(path, timezones.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
			LOG.warn(e.getMessage());
		}

		return new CommandAnswer.Builder()
				.message(NO_TIME_ZONE_FOUND)
				.file(path.toFile())
				.build();
	}

	private CommandAnswer isNotAdminCommandAnswer() {
		return AdminUtility.isNotAdminCommandAnswer();
	}
}
