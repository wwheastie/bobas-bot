package com.bobasalliance.bobasbot.commands.api.payouts.sub.commands;

import com.bobasalliance.bobasbot.commands.beans.CommandAnswer;
import com.bobasalliance.bobasbot.commands.beans.EventDetails;

public interface PayoutsSubCommand {
	String getName();

	CommandAnswer execute(EventDetails eventDetails);
}
