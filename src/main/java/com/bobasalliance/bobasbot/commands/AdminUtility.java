package com.bobasalliance.bobasbot.commands;

import java.util.Set;

import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;

public class AdminUtility {
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

	private AdminUtility() {}
}
