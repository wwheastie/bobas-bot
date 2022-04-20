package com.bobasalliance.bobasbot.commands.commands.payouts.sub.commands;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

@Component
public class PayoutsSubCommandFactory {
	private final Map<String, PayoutsSubCommand> subCommandMap;

	public PayoutsSubCommandFactory(final Set<PayoutsSubCommand> commandSet) {
		this.subCommandMap = commandSet.stream().collect(Collectors.toMap(PayoutsSubCommand::getName, Function.identity()));
	}

	public PayoutsSubCommand getSubCommand(final String name) {
		return subCommandMap.get(name);
	}
}
