package com.bobasalliance.bobasbot.commands.api.payouts.sub.commands;

import java.util.*;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.springframework.stereotype.Component;

import com.bobasalliance.bobasbot.commands.api.payouts.PayoutTimeDao;
import com.bobasalliance.bobasbot.commands.api.payouts.PayoutTimeRepository;
import com.bobasalliance.bobasbot.commands.api.payouts.PayoutsCommandMetadataUtility;
import com.bobasalliance.bobasbot.commands.beans.CommandAnswer;
import com.bobasalliance.bobasbot.commands.beans.EventDetails;
import com.bobasalliance.bobasbot.commands.enums.MessageType;
import com.bobasalliance.bobasbot.commands.factory.DiscordEmbedMessageBuilderFactory;

@Component
public class PayoutsListSubCommand implements PayoutsSubCommand {
	private static final String PAYOUTS_LIST_TITLE = "List of Payouts in order (UTC Time)";
	private static final String NO_USERS_IN_CHANNEL_ERROR_MESSAGE = "No user has been registered in this channel. Please use **/payouts add**";

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

		return new CommandAnswer.Builder()
				.embedMessages(Collections.singletonList(embed))
				.build();
	}

	//	private CommandAnswer listPayouts(Message receivedMessage) {
	//		String channelID = Long.toString(receivedMessage.getChannel().getId());
	//		Map<Integer, String> embedContent = new TreeMap<Integer, String>();
	//
	//		TimeZone utc = TimeZone.getTimeZone("UTC");
	//		Calendar now = getCalendarWithoutDate(utc);
	//
	//		EmbedBuilder embed = new EmbedBuilder();
	//		embed.setColor(EMBED_COLOR);
	//		embed.setTitle(LIST_TITLE);
	//
	//		Connection conn = null;
	//		PreparedStatement stmt = null;
	//		ResultSet rs = null;
	//
	//		ArrayList content = new ArrayList();
	//
	//		try {
	//			conn = StaticVars.getJdbcConnection();
	//
	//			stmt = conn.prepareStatement(SQL_SELECT_CHANNEL_USERS);
	//
	//			stmt.setString(1, channelID);
	//
	//			logger.info("executingQuery " + stmt.toString());
	//			rs = stmt.executeQuery();
	//
	//			boolean hasAnswer = false;
	//			int index = 0;
	//
	//			while (rs.next()) {
	//				hasAnswer = true;
	//
	//				String userName = rs.getString("userName");
	//				Calendar payoutTime = Calendar.getInstance(utc);
	//				payoutTime.setTime(rs.getTime("payoutTime"));
	//				String flag = rs.getString("flag");
	//				String swgohggLink = rs.getString("swgohggLink");
	//
	//				boolean hasLink = StringUtils.isNotBlank(swgohggLink);
	//
	//				StringBuilder builder = new StringBuilder();
	//				builder.append(" " + flag);
	//				if (hasLink) {
	//					builder.append('[');
	//				}
	//				builder.append(" " + userName + " ");
	//				if (hasLink) {
	//					builder.append("](");
	//					builder.append(swgohggLink);
	//					builder.append(")");
	//				}
	//
	//				String contentLine = "";
	//				int poMinute = payoutTime.get(Calendar.MINUTE);
	//				String payoutMinute = String.valueOf(poMinute);
	//				if (payoutMinute.length() == 1)
	//					payoutMinute = "0" + payoutMinute;
	//
	//				if (Integer.toString(payoutTime.get(Calendar.HOUR_OF_DAY)).length() == 1) {
	//
	//					contentLine = "0" + payoutTime.get(Calendar.HOUR_OF_DAY) + ":" + payoutMinute;
	//				} else {
	//					contentLine = payoutTime.get(Calendar.HOUR_OF_DAY) + ":" + payoutMinute;
	//				}
	//
	//				contentLine += builder.toString();
	//
	//				embedContent.put(index, contentLine);
	//				content.add(contentLine);
	//			}
	//
	//			if (!hasAnswer) {
	//				return new CommandAnswer(ERROR_NO_USER_IN_CHAN, null);
	//			}
	//
	//			int i = 0;
	//			int counter = 0;
	//
	//			String modifiedContent = "";
	//			ArrayList<String> sortedContent = sortAscending(content);
	//			while (i < sortedContent.size()) {
	//				modifiedContent += content.get(i) + "\n";
	//				i++;
	//				counter++;
	//				if (counter == 20) {
	//					embed.addField("Times", modifiedContent);
	//					modifiedContent = "";
	//					counter = 0;
	//				}
	//
	//			}
	//
	//			embed.addField("Times", modifiedContent);
	//			return new CommandAnswer(null, embed);
	//		} catch (SQLException e) {
	//			logger.error(e.getMessage());
	//			e.printStackTrace();
	//			return new CommandAnswer(SQL_ERROR, null);
	//		} finally {
	//			try {
	//				if (rs != null) {
	//					rs.close();
	//				}
	//				if (stmt != null) {
	//					stmt.close();
	//				}
	//				if (conn != null) {
	//					conn.close();
	//				}
	//			} catch (SQLException e) {
	//				e.printStackTrace();
	//				logger.error(e.getMessage());
	//			}
	//		}
	//	}
	//
	//	private CommandAnswer payoutTimesCommandAnswerOld(final List<PayoutTimeDao> payoutTimeDaos) {
	//		Map<Integer, String> embedContent = new TreeMap<>();
	//		ArrayList content = new ArrayList();
	//		int index = 0;
	//		EmbedBuilder embed = discordEmbedMessageBuilderFactory.getEmbedMessageBuilder(MessageType.SUCCESS);
	//		embed.setTitle(PAYOUTS_LIST_TITLE);
	//
	//		int j = 0;
	//
	//		for (PayoutTimeDao payoutTimeDao : payoutTimeDaos) {
	//			if (j++ > 15) {
	//				break;
	//			}
	//
	//			StringBuilder stringBuilder = new StringBuilder();
	//			stringBuilder.append(" " + payoutTimeDao.getFlag());
	//
	//			boolean hasLink = StringUtils.isNotEmpty(payoutTimeDao.getSwgohggLink());
	//
	//			if (hasLink) {
	//				stringBuilder.append("[");
	//			}
	//
	//			stringBuilder.append(StringUtils.SPACE + payoutTimeDao.getUserName() + StringUtils.SPACE);
	//
	//			if (hasLink) {
	//				stringBuilder.append("](");
	//				stringBuilder.append(payoutTimeDao.getSwgohggLink());
	//				stringBuilder.append(")");
	//			}
	//
	//			String contentLine;
	//			Calendar payoutTime = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
	//			int poMinute = payoutTime.get(Calendar.MINUTE);
	//			String payoutMinute = String.valueOf(poMinute);
	//			if (payoutMinute.length() == 1)
	//				payoutMinute = "0" + payoutMinute;
	//
	//			if (Integer.toString(payoutTime.get(Calendar.HOUR_OF_DAY)).length() == 1) {
	//
	//				contentLine = "0" + payoutTime.get(Calendar.HOUR_OF_DAY) + ":" + payoutMinute;
	//			} else {
	//				contentLine = payoutTime.get(Calendar.HOUR_OF_DAY) + ":" + payoutMinute;
	//			}
	//
	//			contentLine += stringBuilder.toString();
	//			embedContent.put(index, contentLine);
	//			content.add(contentLine);
	//		}
	//
	//		int i = 0;
	//		int counter = 0;
	//
	//		String modifiedContent = "";
	//		ArrayList<String> sortedContent = sortAscending(content);
	//		while (i < sortedContent.size()) {
	//			modifiedContent += content.get(i) + "\n";
	//			i++;
	//			counter++;
	//			if (counter == 20) {
	//				embed.addField("Times", modifiedContent, false);
	//				modifiedContent = "";
	//				counter = 0;
	//			}
	//
	//		}
	//
	//		embed.addField("Times", modifiedContent, false);
	//		return new CommandAnswer.Builder()
	//				.embedMessages(Collections.singletonList(embed))
	//				.build();
	//	}

	private ArrayList<String> sortAscending(ArrayList arrayList) {
		Collections.sort(arrayList);
		return arrayList;
	}

	private CommandAnswer noUsersInChannelCommandAnswer() {
		EmbedBuilder embedMessage = discordEmbedMessageBuilderFactory.getEmbedMessageBuilder(MessageType.WARNING)
				.setTitle(PAYOUTS_LIST_TITLE)
				.addField("", NO_USERS_IN_CHANNEL_ERROR_MESSAGE, false);
		return new CommandAnswer.Builder()
				.embedMessages(Collections.singletonList(embedMessage))
				.build();
	}
}
