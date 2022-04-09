package com.bobasalliance.bobasbot.commands.factory;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.bobasalliance.bobasbot.commands.api.Command;

@Component
public class CommandFactory {
	private final Set<Command> commandSet;
	private final Map<String, Command> commandMap;

	@Inject
	public CommandFactory(final Set<Command> commandSet) {
		this.commandSet = commandSet;
		this.commandMap = commandSet.stream().collect(Collectors.toMap(Command::getName, Function.identity()));
	}

	public Command getCommand(final String name) {
		return commandMap.get(name);
	}

	public Set<Command> getCommands() {
		return commandSet;
	}
}
