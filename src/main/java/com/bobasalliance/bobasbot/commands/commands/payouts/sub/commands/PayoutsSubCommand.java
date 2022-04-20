package com.bobasalliance.bobasbot.commands.commands.payouts.sub.commands;

import com.bobasalliance.bobasbot.commands.beans.CommandAnswer;
import com.bobasalliance.bobasbot.commands.beans.EventDetails;

public interface PayoutsSubCommand {
	String getName();

	CommandAnswer execute(EventDetails eventDetails);
}
