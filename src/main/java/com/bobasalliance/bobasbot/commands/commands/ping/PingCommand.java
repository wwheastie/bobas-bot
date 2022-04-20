package com.bobasalliance.bobasbot.commands.commands.ping;

import org.springframework.stereotype.Component;

import com.bobasalliance.bobasbot.commands.beans.CommandAnswer;
import com.bobasalliance.bobasbot.commands.beans.CommandMetadata;
import com.bobasalliance.bobasbot.commands.beans.EventDetails;
import com.bobasalliance.bobasbot.commands.commands.Command;

@Component
public class PingCommand implements Command {
	private static final String COMMAND_NAME = "ping";
	private static final String COMMAND_DESCRIPTION = "Health check";
	private static final String SUCCESS_MESSAGE = "Successfully connected!";

	private final CommandMetadata commandMetadata;

	public PingCommand() {
		this.commandMetadata = new CommandMetadata.Builder(COMMAND_NAME, COMMAND_DESCRIPTION)
				.build();
	}

	@Override
	public String getName() {
		return COMMAND_NAME;
	}

	@Override
	public CommandMetadata getMetadata() {
		return commandMetadata;
	}

	@Override
	public CommandAnswer execute(final EventDetails eventDetails) {
		return new CommandAnswer.Builder()
				.message(SUCCESS_MESSAGE)
				.build();
	}
}
