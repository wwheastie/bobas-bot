package com.bobasalliance.bobasbot.commands.api.payouts;

import com.bobasalliance.bobasbot.commands.beans.CommandMetadata;
import com.bobasalliance.bobasbot.commands.beans.CommandOptionMetadata;
import com.bobasalliance.bobasbot.commands.beans.CommandSubCommandMetadata;
import com.bobasalliance.bobasbot.commands.enums.FieldType;

public class PayoutsCommandMetadataUtility {
	public static final String PAYOUTS_COMMAND_NAME = "payouts";
	public static final String PAYOUTS_COMMAND_DESCRIPTION = "Allows management of your arena payouts";

	public static final String LIST_SUB_COMMAND_NAME = "list";
	public static final String LIST_SUB_COMMAND_DESCRIPTION = "Get list of payouts in channel";

	public static CommandMetadata createCommandMetadata() {
		return new CommandMetadata.Builder(PAYOUTS_COMMAND_NAME, PAYOUTS_COMMAND_DESCRIPTION)
				.subCommand(createListSubCommand())
				.build();
	}

	private static CommandSubCommandMetadata createListSubCommand() {
		return createSubCommand(LIST_SUB_COMMAND_NAME, LIST_SUB_COMMAND_DESCRIPTION);
	}

	private static CommandSubCommandMetadata createSubCommand(final String name, final String description) {
		return new CommandSubCommandMetadata.Builder(name, description)
				.build();
	}

	private static CommandOptionMetadata createRequiredStringOption(final String name, final String description) {
		return new CommandOptionMetadata.Builder(name, description)
				.fieldType(FieldType.STRING)
				.required(true)
				.build();
	}

	private PayoutsCommandMetadataUtility() {}
}
