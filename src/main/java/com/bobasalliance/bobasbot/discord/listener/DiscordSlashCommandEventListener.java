package com.bobasalliance.bobasbot.discord.listener;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.javacord.api.entity.message.Message;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.listener.interaction.SlashCommandCreateListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.bobasalliance.bobasbot.commands.api.Command;
import com.bobasalliance.bobasbot.commands.beans.CommandAnswer;
import com.bobasalliance.bobasbot.commands.beans.EventDetails;
import com.bobasalliance.bobasbot.commands.factory.CommandFactory;
import com.bobasalliance.bobasbot.discord.mapper.SlashCommandEventToEventDetailsMapper;

import ma.glasnost.orika.BoundMapperFacade;

@Component
public class DiscordSlashCommandEventListener implements SlashCommandCreateListener {
	private static final Logger LOG = LoggerFactory.getLogger(DiscordSlashCommandEventListener.class);
	private static final String NO_RESPONSE_MESSAGE = "No response was given by the requested command!";
	private static final String UNEXPECTED_ERROR_MESSAGE = "Unexpected error occurred while processing your request.";

	private final CommandFactory commandFactory;
	private final BoundMapperFacade<SlashCommandCreateEvent, EventDetails> slashCommandCreateEventToEventDetailsMapper;

	public DiscordSlashCommandEventListener(final CommandFactory commandFactory,
			final SlashCommandEventToEventDetailsMapper slashCommandEventToEventDetailsMapper) {
		this.commandFactory = commandFactory;
		this.slashCommandCreateEventToEventDetailsMapper = slashCommandEventToEventDetailsMapper.dedicatedMapperFor(SlashCommandCreateEvent.class,
				EventDetails.class);
	}

	@Override
	public void onSlashCommandCreate(final SlashCommandCreateEvent event) {
		try {
			// TODO: Add logging information about event request details
			final Command command = getCommand(event);
			final EventDetails eventDetails = getEventDetails(event);
			final CommandAnswer answer = executeCommand(command, eventDetails);
			reply(event, answer);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			replyErrorMessage(event);
		} finally {
			// TODO: Add insert into command history
		}
	}

	private Command getCommand(final SlashCommandCreateEvent event) {
		return commandFactory.getCommand(event.getSlashCommandInteraction().getCommandName());
	}

	private EventDetails getEventDetails(final SlashCommandCreateEvent event) {
		return slashCommandCreateEventToEventDetailsMapper.map(event);
	}

	private CommandAnswer executeCommand(final Command command, final EventDetails eventDetails) {
		return command.execute(eventDetails);
	}

	private void reply(final SlashCommandCreateEvent event, final CommandAnswer answer) {
		if (answer.hasReactionListener()) {
			final CompletableFuture<Message> future = event.getSlashCommandInteraction().getChannel().get().sendMessage(answer.getMessage());
			future.thenAccept(sentMessage -> {
				answer.getReactionEmojis().forEach(emoji -> sentMessage.addReaction(emoji.getUnicode()));
				sentMessage.addMessageAttachableListener(answer.getReactionListener())
						.forEach(manager -> manager.removeAfter(10, TimeUnit.MINUTES));
			});
		} else if (answer.hasEmbedMessages()) {
			event.getSlashCommandInteraction().createImmediateResponder().addEmbeds(answer.getEmbedMessages()).respond();
		} else if (answer.hasMessage()) {
			event.getSlashCommandInteraction().createImmediateResponder().setContent(answer.getMessage()).respond();
		} else {
			event.getSlashCommandInteraction().createImmediateResponder().setContent(NO_RESPONSE_MESSAGE).respond();
		}
	}

	private void replyErrorMessage(final SlashCommandCreateEvent event) {
		event.getSlashCommandInteraction().createImmediateResponder().setContent(UNEXPECTED_ERROR_MESSAGE).respond();
	}
}
