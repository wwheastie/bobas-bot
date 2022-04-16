package com.bobasalliance.bobasbot.commands.api.payouts;

import org.springframework.stereotype.Component;

import com.bobasalliance.bobasbot.commands.api.Command;
import com.bobasalliance.bobasbot.commands.api.payouts.sub.commands.PayoutsSubCommand;
import com.bobasalliance.bobasbot.commands.api.payouts.sub.commands.PayoutsSubCommandFactory;
import com.bobasalliance.bobasbot.commands.beans.CommandAnswer;
import com.bobasalliance.bobasbot.commands.beans.CommandMetadata;
import com.bobasalliance.bobasbot.commands.beans.EventDetails;

@Component
public class PayoutsCommand implements Command {
	private static final CommandMetadata metadata = PayoutsCommandMetadataUtility.createCommandMetadata();

	private final PayoutsSubCommandFactory payoutSubCommandFactory;

	public PayoutsCommand(final PayoutsSubCommandFactory payoutSubCommandFactory) {
		this.payoutSubCommandFactory = payoutSubCommandFactory;
	}

	@Override
	public String getName() {
		return PayoutsCommandMetadataUtility.PAYOUTS_COMMAND_NAME;
	}

	@Override
	public CommandMetadata getMetadata() {
		return metadata;
	}

	@Override
	public CommandAnswer execute(final EventDetails eventDetails) {
		PayoutsSubCommand subCommand = getSubCommand(eventDetails);
		return subCommand.execute(eventDetails);
	}

	private PayoutsSubCommand getSubCommand(final EventDetails eventDetails) {
		return payoutSubCommandFactory.getSubCommand(eventDetails.getSubCommandName());
	}
}
