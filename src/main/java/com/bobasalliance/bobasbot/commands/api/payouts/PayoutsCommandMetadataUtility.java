package com.bobasalliance.bobasbot.commands.api.payouts;

import java.util.Arrays;

import com.bobasalliance.bobasbot.commands.beans.CommandMetadata;
import com.bobasalliance.bobasbot.commands.beans.CommandOptionMetadata;
import com.bobasalliance.bobasbot.commands.beans.CommandSubCommandMetadata;
import com.bobasalliance.bobasbot.commands.enums.FieldType;

public class PayoutsCommandMetadataUtility {
	public static final String PAYOUTS_COMMAND_NAME = "payouts";
	public static final String PAYOUTS_COMMAND_DESCRIPTION = "Allows management of your arena payouts";

	public static final String LIST_SUB_COMMAND_NAME = "list";
	public static final String LIST_SUB_COMMAND_DESCRIPTION = "Get list of payouts in channel";

	public static final String DELETE_SUB_COMMAND_NAME = "delete";
	public static final String DELETE_SUB_COMMAND_DESCRIPTION = "Delete user from payouts list in channel";

	public static final String USER_NAME_COMMAND_OPTION = "user_name";
	public static final String USER_NAME_COMMAND_DESCRIPTION = "User name to perform action";

	public static CommandMetadata createCommandMetadata() {
		return new CommandMetadata.Builder(PAYOUTS_COMMAND_NAME, PAYOUTS_COMMAND_DESCRIPTION)
				.subCommand(createListSubCommand())
				.subCommand(createDeleteSubCommand())
				.build();
	}

	private static CommandSubCommandMetadata createListSubCommand() {
		return createSubCommand(LIST_SUB_COMMAND_NAME, LIST_SUB_COMMAND_DESCRIPTION);
	}

	private static CommandSubCommandMetadata createDeleteSubCommand() {
		return new CommandSubCommandMetadata.Builder(DELETE_SUB_COMMAND_NAME, DELETE_SUB_COMMAND_DESCRIPTION)
				.options(Arrays.asList(createRequiredStringCommandOption(USER_NAME_COMMAND_OPTION, USER_NAME_COMMAND_DESCRIPTION)))
				.build();
	}

	private static CommandSubCommandMetadata createSubCommand(final String name, final String description) {
		return new CommandSubCommandMetadata.Builder(name, description)
				.build();
	}

	private static CommandOptionMetadata createRequiredStringCommandOption(final String name, final String description) {
		return new CommandOptionMetadata.Builder(name, description)
				.fieldType(FieldType.STRING)
				.required(true)
				.build();
	}

	private PayoutsCommandMetadataUtility() {}
}
