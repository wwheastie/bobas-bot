package com.bobasalliance.bobasbot.commands.commands.payouts.sub.commands;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
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
		List<PayoutTimeDao> payoutTimes = payoutTimeRepository.findAllByChannelIdOrderByPayoutTimeAndUserName(eventDetails.getChannelId());
		return CollectionUtils.isNotEmpty(payoutTimes) ? payoutTimesCommandAnswer(payoutTimes) : noUsersInChannelCommandAnswer();
	}

	private CommandAnswer payoutTimesCommandAnswer(final List<PayoutTimeDao> payoutTimeDaos) {
		Map<Integer, String> embedContent = new TreeMap<Integer, String>();
		TimeZone utc = TimeZone.getTimeZone("UTC");
		EmbedBuilder embed = discordEmbedMessageBuilderFactory.getEmbedMessageBuilder(MessageType.SUCCESS);
		embed.setTitle(PAYOUTS_LIST_TITLE);
		ArrayList content = new ArrayList();
		int index = 0;

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

			contentLine += builder.toString();

			embedContent.put(index, contentLine);
			content.add(contentLine);
		}

		int i = 0;
		int counter = 0;

		String modifiedContent = "";
		ArrayList<String> sortedContent = sortAscending(content);
		while (i < sortedContent.size()) {
			modifiedContent += content.get(i) + "\n";
			i++;
			counter++;
			if (counter == 20) {
				embed.addField("Times", modifiedContent);
				modifiedContent = "";
				counter = 0;
			}
		}

		embed.addField("Times", modifiedContent, false);

		DiscordMessageUtility.addFooter(embed);

		return new CommandAnswer.Builder()
				.embedMessages(Collections.singletonList(embed))
				.build();
	}

	private ArrayList<String> sortAscending(ArrayList arrayList) {
		Collections.sort(arrayList);
		return arrayList;
	}

	private CommandAnswer noUsersInChannelCommandAnswer() {
		return new CommandAnswer.Builder()
				.message(NO_USERS_IN_CHANNEL_ERROR_MESSAGE)
				.build();
	}
}
