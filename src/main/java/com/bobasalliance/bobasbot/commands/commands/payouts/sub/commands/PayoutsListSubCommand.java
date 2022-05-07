package com.bobasalliance.bobasbot.commands.commands.payouts.sub.commands;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.springframework.stereotype.Component;

import com.bobasalliance.bobasbot.commands.DiscordMessageUtility;
import com.bobasalliance.bobasbot.commands.beans.CommandAnswer;
import com.bobasalliance.bobasbot.commands.beans.EventDetails;
import com.bobasalliance.bobasbot.commands.commands.payouts.PayoutTimeDao;
import com.bobasalliance.bobasbot.commands.commands.payouts.PayoutTimeRepository;
import com.bobasalliance.bobasbot.commands.commands.payouts.PayoutsCommandMetadataUtility;
import com.bobasalliance.bobasbot.commands.enums.MessageType;
import com.bobasalliance.bobasbot.commands.factory.DiscordEmbedMessageBuilderFactory;

@Component
public class PayoutsListSubCommand implements PayoutsSubCommand {
	private static final int MAX_FIELD_LENGTH = 1000;
	private static final String CONTENT_LENGTH_TOO_LONG_ERROR = "Individual content line too long";
	private static final String PAYOUTS_LIST_TITLE = "List of Payouts in order (UTC Time)";
	private static final String NO_USERS_IN_CHANNEL_ERROR_MESSAGE = "No users has been registered in this channel. Please use **/payouts add**";

	private final PayoutTimeRepository payoutTimeRepository;
	private final DiscordEmbedMessageBuilderFactory discordEmbedMessageBuilderFactory;

	public PayoutsListSubCommand(final PayoutTimeRepository payoutTimeRepository, final DiscordEmbedMessageBuilderFactory discordEmbedMessageBuilderFactory) {
		this.payoutTimeRepository = payoutTimeRepository;
		this.discordEmbedMessageBuilderFactory = discordEmbedMessageBuilderFactory;
	}

	@Override
	public String getName() {
		return PayoutsCommandMetadataUtility.LIST_SUB_COMMAND_NAME;
	}

	@Override
	public CommandAnswer execute(final EventDetails eventDetails) {
		List<PayoutTimeDao> payoutTimes = payoutTimeRepository
				.findAllByChannelIdOrderByPayoutTimeAndUserName(eventDetails.getChannelId());
		return CollectionUtils.isNotEmpty(payoutTimes) ? payoutTimesCommandAnswer(payoutTimes) : noUsersInChannelCommandAnswer();
	}

	private CommandAnswer payoutTimesCommandAnswer(final List<PayoutTimeDao> payoutTimeDaos) {
		Map<Integer, String> embedContent = new TreeMap<Integer, String>();
		TimeZone utc = TimeZone.getTimeZone("UTC");
		ArrayList<UserInfo> content = new ArrayList();
		int index = 0;

		Calendar now = getCalendarWithoutDate(utc);

		for (final PayoutTimeDao payoutTimeDao : payoutTimeDaos) {
			String userName = payoutTimeDao.getUserName();
			Calendar payoutTime = Calendar.getInstance(utc);
			payoutTime.setTime(payoutTimeDao.getPayoutTime());
			String flag = payoutTimeDao.getFlag();
			String swgohggLink = payoutTimeDao.getSwgohggLink();

			boolean hasLink = StringUtils.isNotBlank(swgohggLink);

			StringBuilder builder = new StringBuilder();
			builder.append(" " + flag);
			if (hasLink) {
				builder.append('[');
			}
			builder.append(" " + userName + " ");
			if (hasLink) {
				builder.append("](");
				builder.append(swgohggLink);
				builder.append(")");
			}

			String contentLine;
			int poMinute = payoutTime.get(Calendar.MINUTE);
			String payoutMinute = String.valueOf(poMinute);
			if (payoutMinute.length() == 1)
				payoutMinute = "0" + payoutMinute;

			if (Integer.toString(payoutTime.get(Calendar.HOUR_OF_DAY)).length() == 1) {

				contentLine = "0" + payoutTime.get(Calendar.HOUR_OF_DAY) + ":" + payoutMinute;
			} else {
				contentLine = payoutTime.get(Calendar.HOUR_OF_DAY) + ":" + payoutMinute;
			}

			embedContent.put(index, contentLine);
			content.add(new UserInfo(contentLine, builder.toString(), getCountdown(payoutTime, now)));
		}

		ArrayList<EmbedBuilder> embedMessages = new ArrayList<>();

		int i = 0;
		int counter = 0;

		String modifiedContent = "";
		ArrayList<UserInfo> sortedContent = sortAscending(content);
		while (i < sortedContent.size()) {
			if (sortedContent.get(i).toString().length() > MAX_FIELD_LENGTH) {
				return new CommandAnswer.Builder()
						.message(CONTENT_LENGTH_TOO_LONG_ERROR)
						.build();
			}

			if (modifiedContent.length() + content.get(i).toString().length() > MAX_FIELD_LENGTH) {
				EmbedBuilder embedMessage = discordEmbedMessageBuilderFactory.getEmbedMessageBuilder(MessageType.SUCCESS);
				embedMessage.setTitle(PAYOUTS_LIST_TITLE);
				embedMessage.addField("Times", modifiedContent);
				DiscordMessageUtility.addFooter(embedMessage);
				modifiedContent = "";
				counter = 0;
				embedMessages.add(embedMessage);
			} else {
				modifiedContent += sortedContent.get(i).toString() + "\n";
				i++;
				counter++;
			}
		}

		EmbedBuilder embedMessage = discordEmbedMessageBuilderFactory.getEmbedMessageBuilder(MessageType.SUCCESS);
		embedMessage.setTitle(PAYOUTS_LIST_TITLE);
		embedMessage.addField("Times", modifiedContent);
		DiscordMessageUtility.addFooter(embedMessage);
		embedMessages.add(embedMessage);

		return new CommandAnswer.Builder()
				.embedMessages(embedMessages)
				.build();
	}

	private String getCountdown(final Calendar payoutTime, final Calendar currentTime) {
		Calendar countDownCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		countDownCalendar.setTime(payoutTime.getTime());

		if (payoutTime.before(currentTime)) {
			countDownCalendar.add(Calendar.DAY_OF_MONTH, 1);
		}

		Long difference = countDownCalendar.getTimeInMillis() - currentTime.getTimeInMillis();

		Long hoursDifference = difference / (60 * 60 * 1000) % 24;
		Long minutesDifference = difference / (60 * 1000) % 60;

		if (minutesDifference < 0) {
			minutesDifference += 60;
			hoursDifference += 23;
		}

		String strHours = StringUtils.leftPad(hoursDifference.toString(), 2, "0");
		String strMinutes = StringUtils.leftPad(minutesDifference.toString(), 2, "0");

		return String.format("`%s:%s` ", strHours, strMinutes);
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

	private ArrayList<UserInfo> sortAscending(final ArrayList<UserInfo> arrayList) {
		Collections.sort(arrayList);
		return arrayList;
	}

	private CommandAnswer noUsersInChannelCommandAnswer() {
		return new CommandAnswer.Builder()
				.message(NO_USERS_IN_CHANNEL_ERROR_MESSAGE)
				.build();
	}

	private class UserInfo implements Comparable<UserInfo> {
		private static final String format = "%s%s [%s]";
		private final String payoutTime;
		private final String flagAndName;
		private final String countDown;

		public UserInfo(final String payoutTime, final String flagAndName, final String countDown) {
			this.payoutTime = payoutTime;
			this.flagAndName = flagAndName;
			this.countDown = countDown;
		}

		public String getPayoutTime() {
			return payoutTime;
		}

		public String getFlagAndName() {
			return flagAndName;
		}

		public String getCountDown() {
			return countDown;
		}

		@Override
		public String toString() {
			return String.format(format, payoutTime, flagAndName, countDown);
		}

		@Override
		public int compareTo(final UserInfo otherUserInfo) {
			return String.CASE_INSENSITIVE_ORDER.compare(this.getCountDown(), otherUserInfo.getCountDown());
		}
	}
}
