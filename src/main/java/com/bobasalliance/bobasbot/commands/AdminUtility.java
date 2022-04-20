package com.bobasalliance.bobasbot.commands;

import java.util.Set;

import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;

import com.bobasalliance.bobasbot.commands.beans.CommandAnswer;

public class AdminUtility {
	private static final String NON_ADMIN_USER_MESSAGE = "You may not execute this command.\nYou must be part of the «botAdmins» Discord group to administrate the bot.";

	private static final Set<Integer> ADMIN_USERS = Set.of(8591, 8431);
	private static final Set<String> ADMIN_GROUP = Set.of("botadmins");

	public static boolean isAdmin(final User author, final Server server) {
		if (server == null) {
			return true;
		}

		if (ADMIN_USERS.contains(author.getDiscriminator())) {
			return true;
		}

		for (Role role : author.getRoles(server)) {
			if (ADMIN_GROUP.contains(role.getName().toLowerCase())) {
				return true;
			}
		}

		return false;
	}

	public static CommandAnswer isNotAdminCommandAnswer() {
		return new CommandAnswer.Builder().message(NON_ADMIN_USER_MESSAGE).build();
	}

	private AdminUtility() {}
}
