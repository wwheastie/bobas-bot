package com.bobasalliance.bobasbot.discord.mapper;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.SlashCommandInteractionOption;
import org.springframework.stereotype.Component;

import com.bobasalliance.bobasbot.commands.beans.EventDetails;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.ObjectFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;
import ma.glasnost.orika.metadata.TypeFactory;

@Component
public class SlashCommandEventToEventDetailsMapper extends ConfigurableMapper {
	@Override
	protected void configure(final MapperFactory mapperFactory) {
		mapperFactory.registerObjectFactory(new EventDetailsFactory(), TypeFactory.valueOf(EventDetails.class));

		mapperFactory.classMap(SlashCommandCreateEvent.class, EventDetails.class)
				.register();
	}

	private class EventDetailsFactory implements ObjectFactory<EventDetails> {
		@Override
		public EventDetails create(final Object source, final MappingContext mappingContext) {
			SlashCommandCreateEvent slashCommandCreateEvent = (SlashCommandCreateEvent) source;

			return new EventDetails.Builder()
					.options(getOptionsMap(slashCommandCreateEvent))
					.user(slashCommandCreateEvent.getSlashCommandInteraction().getUser())
					.channelId(slashCommandCreateEvent.getSlashCommandInteraction().getChannel().get().getIdAsString())
					.subCommandName(getSubCommand(slashCommandCreateEvent))
					.build();
		}

		private Map<String, String> getOptionsMap(final SlashCommandCreateEvent event) {
			return event.getSlashCommandInteraction()
					.getArguments()
					.stream()
					.filter(slashCommandInteractionOption -> slashCommandInteractionOption.getStringValue().isPresent())
					.collect(Collectors.toMap(SlashCommandInteractionOption::getName,
							slashCommandInteractionOption -> slashCommandInteractionOption.getStringValue().get()));
		}

		private String getSubCommand(final SlashCommandCreateEvent event) {
			Optional<String> optionalSubCommand = event.getSlashCommandInteraction()
					.getOptions()
					.stream()
					.filter(slashCommandInteractionOption -> StringUtils.isNotEmpty(slashCommandInteractionOption.getName()))
					.map(SlashCommandInteractionOption::getName)
					.findFirst();

			return optionalSubCommand.orElse(StringUtils.EMPTY);
		}
	}
}
