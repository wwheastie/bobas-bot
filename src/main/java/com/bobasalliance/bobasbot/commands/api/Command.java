package com.bobasalliance.bobasbot.commands.api;

import com.bobasalliance.bobasbot.commands.beans.CommandAnswer;
import com.bobasalliance.bobasbot.commands.beans.CommandMetadata;
import com.bobasalliance.bobasbot.commands.beans.EventDetails;

public interface Command {
	String getName();

	CommandMetadata getMetadata();

	CommandAnswer execute(EventDetails eventDetails);
}
