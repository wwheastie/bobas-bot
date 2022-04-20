package com.bobasalliance.bobasbot.commands.commands.payouts;

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

	public static final String ADD_SUB_COMMAND_NAME = "add";
	public static final String ADD_SUB_COMMAND_DESCRIPTION = "Add user to payouts list in channel";

	public static final String USER_NAME_COMMAND_OPTION_NAME = "user_name";
	public static final String USER_NAME_COMMAND_DESCRIPTION = "User name to perform action";

	public static final String PAYOUT_TIME_COMMAND_OPTION_NAME = "payout_time";
	public static final String PAYOUT_TIME_COMMAND_OPTION_DESCRIPTION = "User payout time (not including time zone)";

	public static final String PAYOUT_TIME_ZONE_COMMAND_OPTION_NAME = "payout_time_zone";
	public static final String PAYOUT_TIME_ZONE_COMMAND_OPTION_DESCRIPTION = "User payout time zone";

	public static final String COUNTRY_FLAG_COMMAND_OPTION_NAME = "country_flag";
	public static final String COUNTRY_FLAG_COMMAND_OPTION_DESCRIPTION = "User country flag";

	public static final String SWGOHGG_LINK_COMMAND_OPTION_NAME = "swgohgg_link";
	public static final String SWGOHGG_LINK_COMMAND_OPTION_DESCRIPTION = "swgoh.gg link to user profile";

	public static CommandMetadata createCommandMetadata() {
		return new CommandMetadata.Builder(PAYOUTS_COMMAND_NAME, PAYOUTS_COMMAND_DESCRIPTION)
				.subCommand(createListSubCommand())
				.subCommand(createDeleteSubCommand())
				.subCommand(createAddSubCommand())
				.build();
	}

	private static CommandSubCommandMetadata createListSubCommand() {
		return new CommandSubCommandMetadata.Builder(LIST_SUB_COMMAND_NAME, LIST_SUB_COMMAND_DESCRIPTION)
				.build();
	}

	private static CommandSubCommandMetadata createDeleteSubCommand() {
		return new CommandSubCommandMetadata.Builder(DELETE_SUB_COMMAND_NAME, DELETE_SUB_COMMAND_DESCRIPTION)
				.options(Arrays.asList(createRequiredStringCommandOption(USER_NAME_COMMAND_OPTION_NAME, USER_NAME_COMMAND_DESCRIPTION)))
				.build();
	}

	private static CommandSubCommandMetadata createAddSubCommand() {
		CommandOptionMetadata userNameOption = createRequiredStringCommandOption(USER_NAME_COMMAND_OPTION_NAME, USER_NAME_COMMAND_DESCRIPTION);
		CommandOptionMetadata payoutTimeOption = createRequiredStringCommandOption(PAYOUT_TIME_COMMAND_OPTION_NAME, PAYOUT_TIME_COMMAND_OPTION_DESCRIPTION);
		CommandOptionMetadata payoutTimeZoneOption = createRequiredStringCommandOption(PAYOUT_TIME_ZONE_COMMAND_OPTION_NAME,
				PAYOUT_TIME_ZONE_COMMAND_OPTION_DESCRIPTION);
		CommandOptionMetadata countryFlag = createNonRequiredStringCommandOption(COUNTRY_FLAG_COMMAND_OPTION_NAME, COUNTRY_FLAG_COMMAND_OPTION_DESCRIPTION);
		CommandOptionMetadata swgohggLink = createNonRequiredStringCommandOption(SWGOHGG_LINK_COMMAND_OPTION_NAME, SWGOHGG_LINK_COMMAND_OPTION_DESCRIPTION);

		return new CommandSubCommandMetadata.Builder(ADD_SUB_COMMAND_NAME, ADD_SUB_COMMAND_DESCRIPTION)
				.options(Arrays.asList(userNameOption, payoutTimeOption, payoutTimeZoneOption, countryFlag, swgohggLink))
				.build();
	}

	private static CommandOptionMetadata createRequiredStringCommandOption(final String name, final String description) {
		return new CommandOptionMetadata.Builder(name, description)
				.fieldType(FieldType.STRING)
				.required(true)
				.build();
	}

	private static CommandOptionMetadata createNonRequiredStringCommandOption(final String name, final String description) {
		return new CommandOptionMetadata.Builder(name, description)
				.fieldType(FieldType.STRING)
				.required(false)
				.build();
	}

	private PayoutsCommandMetadataUtility() {}
}
