package com.bobasalliance.bobasbot.commands.api.payouts;

import org.springframework.stereotype.Component;

import com.bobasalliance.bobasbot.commands.beans.CommandAnswer;
import com.bobasalliance.bobasbot.commands.beans.EventDetails;
import com.bobasalliance.bobasbot.commands.database.PayoutTimeRepository;

@Component
public class PayoutsListSubCommand implements PayoutsSubCommand {
	private final PayoutTimeRepository payoutTimeRepository;

	public PayoutsListSubCommand(final PayoutTimeRepository payoutTimeRepository) {
		this.payoutTimeRepository = payoutTimeRepository;
	}

	@Override
	public String getName() {
		return PayoutsCommandMetadataUtility.LIST_SUB_COMMAND_NAME;
	}

	@Override
	public CommandAnswer execute(final EventDetails eventDetails) {
		//		List<PayoutTimeDao> payoutTimes = payoutTimeRepository.findAllByChannelIdOrderByPayoutTimeAndUserName(eventDetails.getChannelId());
		//		EmbedBuilder embedMessage = CollectionUtils.isEmpty(payoutTimes) ? noUsersInChannelEmbedMessage() : payoutTimesEmbedMessage(payoutTimes);
		//		return new CommandAnswer.Builder()
		//				.embedMessages(Collections.singletonList(embedMessage))
		//				.build();
		return null;
	}
}
