package com.bobasalliance.bobasbot.commands;

import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.springframework.stereotype.Component;

import com.bobasalliance.bobasbot.commands.beans.CommandAnswer;
import com.bobasalliance.bobasbot.discord.configuration.Settings;

@Component
public class AdminService {
	private static final String NON_ADMIN_USER_MESSAGE = "You may not execute this command.\nYou must be part of the «botAdmins» Discord group to administrate the bot.";

	private final Settings settings;

	public AdminService(final Settings settings) {
		this.settings = settings;
	}

	public boolean isAdmin(final User author, final Server server) {
		if (server == null) {
			return true;
		}

		if (settings.getBotAdminsUsers().contains(author.getDiscriminator())) {
			return true;
		}

		for (Role role : author.getRoles(server)) {
			if (settings.getBotAdminsGroup().contains(role.getName().toLowerCase())) {
				return true;
			}
		}

		return false;
	}

	public CommandAnswer isNotAdminCommandAnswer() {
		return new CommandAnswer.Builder().message(NON_ADMIN_USER_MESSAGE).build();
	}
}
