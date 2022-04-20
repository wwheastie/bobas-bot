package com.bobasalliance.bobasbot.discord.listener;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.javacord.api.entity.message.Message;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandInteractionOption;
import org.javacord.api.listener.interaction.SlashCommandCreateListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.bobasalliance.bobasbot.commands.beans.CommandAnswer;
import com.bobasalliance.bobasbot.commands.beans.EventDetails;
import com.bobasalliance.bobasbot.commands.commands.Command;
import com.bobasalliance.bobasbot.commands.factory.CommandFactory;
import com.bobasalliance.bobasbot.discord.mapper.SlashCommandEventToEventDetailsMapper;
import com.bobasalliance.bobasbot.discord.repository.CommandHistoryDao;
import com.bobasalliance.bobasbot.discord.repository.CommandHistoryRepository;

import ma.glasnost.orika.BoundMapperFacade;

@Component
public class DiscordSlashCommandEventListener implements SlashCommandCreateListener {
	private static final Logger LOG = LoggerFactory.getLogger(DiscordSlashCommandEventListener.class);
	private static final String NO_RESPONSE_MESSAGE = "No response was given by the requested command!";
	private static final String UNEXPECTED_ERROR_MESSAGE = "Unexpected error occurred while processing your request.";

	private final CommandFactory commandFactory;
	private final CommandHistoryRepository commandHistoryRepository;
	private final BoundMapperFacade<SlashCommandCreateEvent, EventDetails> slashCommandCreateEventToEventDetailsMapper;

	public DiscordSlashCommandEventListener(final CommandFactory commandFactory, final CommandHistoryRepository commandHistoryRepository,
			final SlashCommandEventToEventDetailsMapper slashCommandEventToEventDetailsMapper) {
		this.commandFactory = commandFactory;
		this.commandHistoryRepository = commandHistoryRepository;
		this.slashCommandCreateEventToEventDetailsMapper = slashCommandEventToEventDetailsMapper.dedicatedMapperFor(SlashCommandCreateEvent.class,
				EventDetails.class);
	}

	@Override
	public void onSlashCommandCreate(final SlashCommandCreateEvent event) {
		final long startTimestamp = System.currentTimeMillis();
		try {
			logCommandRequest(event.getSlashCommandInteraction());
			final Command command = getCommand(event);
			final EventDetails eventDetails = getEventDetails(event);
			final CommandAnswer answer = executeCommand(command, eventDetails);
			reply(event, answer);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			replyErrorMessage(event);
		} finally {
			insertCommandHistory(event.getSlashCommandInteraction(), startTimestamp);
		}
	}

	private void logCommandRequest(final SlashCommandInteraction interaction) {
		String logMessage = String.format("onMessageCreate "
				+ "channel=%s "
				+ "server=%s "
				+ "authorId=%s "
				+ "authorDiscordUser=%s "
				+ "message='%s'",
				interaction.getChannel().get().getIdAsString(),
				interaction.getServer().orElse(null),
				interaction.getUser().getIdAsString(),
				interaction.getUser().getDiscriminatedName(),
				interaction.getCommandName() + " " + StringUtils.joinWith(" ",
						interaction.getArguments().stream().map(SlashCommandInteractionOption::getStringValue).collect(Collectors.toList())));
		LOG.info(logMessage);
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
		} else if (answer.hasFile()) {
			if (answer.hasMessage()) {
				event.getSlashCommandInteraction().getChannel().get().sendMessage(answer.getMessage());
			}
			event.getSlashCommandInteraction().getChannel().get().sendMessage(answer.getFile());
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

	private void insertCommandHistory(final SlashCommandInteraction interaction, final long startTimestamp) {
		final CommandHistoryDao dao = createCommandHistoryDao(interaction, startTimestamp);
		commandHistoryRepository.insert(dao.getCommand(), dao.getTimestamp(), dao.getUserId(), dao.getUserName(),
				dao.getServerId(), dao.getServerName(), dao.getServerRegion(), dao.getExecutionTime());
	}

	private CommandHistoryDao createCommandHistoryDao(final SlashCommandInteraction interaction, final long startTimestamp) {
		CommandHistoryDao dao = new CommandHistoryDao();
		dao.setCommand(interaction.getCommandName());
		dao.setTimestamp(new Timestamp(new Date().getTime()));
		dao.setUserId(interaction.getUser().getIdAsString());
		dao.setUserName(interaction.getUser().getName());
		dao.setServerId(interaction.getServer().get().getIdAsString());
		dao.setServerName(interaction.getServer().get().getName());
		dao.setServerRegion(interaction.getServer().get().getRegion().getKey().toUpperCase(Locale.ROOT));
		dao.setExecutionTime(System.currentTimeMillis() - startTimestamp);
		return dao;
	}
}
