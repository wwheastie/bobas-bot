package com.bobasalliance.bobasbot.discord.mapper;

import org.springframework.stereotype.Component;

import com.bobasalliance.bobasbot.commands.beans.CommandMetadata;
import com.bobasalliance.bobasbot.commands.beans.CommandOptionChoiceMetadata;
import com.bobasalliance.bobasbot.commands.beans.CommandOptionMetadata;
import com.bobasalliance.bobasbot.commands.beans.CommandSubCommandMetadata;
import com.bobasalliance.bobasbot.discord.beans.DiscordCommandMetadata;
import com.bobasalliance.bobasbot.discord.beans.DiscordCommandOptionChoiceMetadata;
import com.bobasalliance.bobasbot.discord.beans.DiscordCommandOptionMetadata;
import com.bobasalliance.bobasbot.discord.enums.Type;

import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.impl.ConfigurableMapper;

@Component
public class DiscordCommandMetadataMapper extends ConfigurableMapper {
	@Override
	protected void configure(final MapperFactory mapperFactory) {
		mapperFactory.classMap(CommandMetadata.class, DiscordCommandMetadata.class)
				.exclude("subCommands")
				.exclude("options")
				.byDefault()
				.register();

		mapperFactory.classMap(CommandSubCommandMetadata.class, DiscordCommandOptionMetadata.class)
				.customize(new SubCommandToDiscordCommandOptionMapper())
				.byDefault()
				.register();

		mapperFactory.classMap(CommandOptionMetadata.class, DiscordCommandOptionMetadata.class)
				.customize(new CommandOptionToDiscordCommandOptionMapper())
				.byDefault()
				.register();

		mapperFactory.classMap(CommandOptionChoiceMetadata.class, DiscordCommandOptionChoiceMetadata.class)
				.byDefault()
				.register();
	}

	private class CommandOptionToDiscordCommandOptionMapper extends CustomMapper<CommandOptionMetadata, DiscordCommandOptionMetadata> {
		@Override
		public void mapAtoB(final CommandOptionMetadata commandOptionMetadata, final DiscordCommandOptionMetadata discordCommandOptionMetadata,
				final MappingContext context) {
			discordCommandOptionMetadata.setType(Type.valueOf(commandOptionMetadata.getFieldType().name()).getValue());
		}
	}

	private class SubCommandToDiscordCommandOptionMapper extends CustomMapper<CommandSubCommandMetadata, DiscordCommandOptionMetadata> {
		@Override
		public void mapAtoB(final CommandSubCommandMetadata commandSubCommandMetadata, final DiscordCommandOptionMetadata discordCommandOptionMetadata,
				final MappingContext context) {
			discordCommandOptionMetadata.setType(Type.SUB_COMMAND.getValue());
			discordCommandOptionMetadata.setRequired(null);
		}
	}
}
