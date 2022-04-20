package com.bobasalliance.bobasbot.discord.registrants;

import java.util.List;

import org.springframework.stereotype.Component;

import com.bobasalliance.bobasbot.commands.beans.CommandMetadata;
import com.bobasalliance.bobasbot.commands.commands.Command;
import com.bobasalliance.bobasbot.commands.factory.CommandFactory;
import com.bobasalliance.bobasbot.discord.beans.DiscordCommandMetadata;
import com.bobasalliance.bobasbot.discord.beans.DiscordCommandOptionMetadata;
import com.bobasalliance.bobasbot.discord.client.DiscordClient;
import com.bobasalliance.bobasbot.discord.mapper.DiscordCommandMetadataMapper;

import ma.glasnost.orika.BoundMapperFacade;

@Component
public class GlobalDiscordCommandRegistrant implements DiscordCommandRegistrant {
	private final DiscordClient discordClient;
	private final CommandFactory commandFactory;
	private final DiscordCommandMetadataMapper discordCommandMetadataMapper;
	private final BoundMapperFacade<CommandMetadata, DiscordCommandMetadata> commandMetadataToDiscordCommandMetadataMapper;

	public GlobalDiscordCommandRegistrant(final DiscordClient discordClient, final CommandFactory commandFactory,
			final DiscordCommandMetadataMapper discordCommandMetadataMapper) {
		this.discordClient = discordClient;
		this.commandFactory = commandFactory;
		this.discordCommandMetadataMapper = discordCommandMetadataMapper;
		this.commandMetadataToDiscordCommandMetadataMapper = discordCommandMetadataMapper.dedicatedMapperFor(CommandMetadata.class,
				DiscordCommandMetadata.class);
	}

	@Override
	public void registerSlashCommands() {
		commandFactory.getCommands().forEach(this::register);
	}

	private void register(final Command command) {
		DiscordCommandMetadata discordCommandMetadata = getDiscordCommandMetadata(command.getMetadata());
		discordClient.registerGlobalSlashCommand(discordCommandMetadata);
	}

	private DiscordCommandMetadata getDiscordCommandMetadata(final CommandMetadata commandMetadata) {
		if (commandMetadata.hasSubCommands()) {
			List<DiscordCommandOptionMetadata> discordCommandOptionMetadataList = discordCommandMetadataMapper.mapAsList(commandMetadata.getSubCommands(),
					DiscordCommandOptionMetadata.class);

			DiscordCommandMetadata discordCommandMetadata = commandMetadataToDiscordCommandMetadataMapper.map(commandMetadata);
			discordCommandMetadata.setOptions(discordCommandOptionMetadataList);

			return discordCommandMetadata;
		} else if (commandMetadata.hasOptions()) {
			List<DiscordCommandOptionMetadata> discordCommandOptionMetadataList = discordCommandMetadataMapper.mapAsList(commandMetadata.getOptions(),
					DiscordCommandOptionMetadata.class);

			DiscordCommandMetadata discordCommandMetadata = commandMetadataToDiscordCommandMetadataMapper.map(commandMetadata);
			discordCommandMetadata.setOptions(discordCommandOptionMetadataList);

			return discordCommandMetadata;
		} else {
			return commandMetadataToDiscordCommandMetadataMapper.map(commandMetadata);
		}
	}
}
