package com.bobasalliance.bobasbot.commands.commands.mods;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.springframework.stereotype.Component;

import com.bobasalliance.bobasbot.commands.DiscordMessageUtility;
import com.bobasalliance.bobasbot.commands.beans.CommandAnswer;
import com.bobasalliance.bobasbot.commands.beans.CommandMetadata;
import com.bobasalliance.bobasbot.commands.beans.CommandOptionMetadata;
import com.bobasalliance.bobasbot.commands.beans.EventDetails;
import com.bobasalliance.bobasbot.commands.commands.Command;
import com.bobasalliance.bobasbot.commands.enums.FieldType;
import com.bobasalliance.bobasbot.commands.enums.MessageType;
import com.bobasalliance.bobasbot.commands.factory.DiscordEmbedMessageBuilderFactory;

@Component
public class ModsCommand implements Command {
	private static final String COMMAND_NAME = "mods";
	private static final String COMMAND_DESCRIPTION = "Mod advice for requested character";
	private static final String CHARACTER_OPTION_NAME = "character";
	private static final String CHARACTER_OPTION_DESCRIPTION = "Character name";
	private static final String CATEGORY_SEPARATOR = "-";
	private static final String MOD_VARIANT_TITLE = "variant %s";
	private static final int MAX_EMBED_MESSAGES = 10;
	private static final int FIRST_VARIANT = 0;
	private static final double REQUESTED_CHARACTER_PROXIMITY = 0.80;

	private final CharacterMatchService characterMatchService;
	private final CommandMetadata commandMetadata;
	private final DiscordEmbedMessageBuilderFactory discordEmbedMessageBuilderFactory;
	private final CharacterModsRepository characterModsRepository;
	private final CharacterModsMapper characterModsMapper;

	@Inject
	public ModsCommand(final CharacterMatchService characterMatchService, final DiscordEmbedMessageBuilderFactory discordEmbedMessageBuilderFactory,
			final CharacterModsRepository characterModsRepository, final CharacterModsMapper characterModsMapper) {
		this.characterMatchService = characterMatchService;
		this.commandMetadata = createCommandMetadata();
		this.discordEmbedMessageBuilderFactory = discordEmbedMessageBuilderFactory;
		this.characterModsRepository = characterModsRepository;
		this.characterModsMapper = characterModsMapper;
	}

	private CommandMetadata createCommandMetadata() {
		final CommandOptionMetadata characterOption = new CommandOptionMetadata.Builder(CHARACTER_OPTION_NAME, CHARACTER_OPTION_DESCRIPTION)
				.fieldType(FieldType.STRING)
				.required(true)
				.build();

		return new CommandMetadata.Builder(COMMAND_NAME, COMMAND_DESCRIPTION)
				.options(Collections.singletonList(characterOption))
				.build();
	}

	@Override
	public String getName() {
		return COMMAND_NAME;
	}

	@Override
	public CommandMetadata getMetadata() {
		return commandMetadata;
	}

	@Override
	public CommandAnswer execute(final EventDetails eventDetails) {
		final String requestedCharacter = getRequestedCharacter(eventDetails);
		final CharacterMatches matches = getCharacterMatches(requestedCharacter);
		final List<EmbedBuilder> discordEmbedMessages = createDiscordEmbedMessages(matches);
		return createCommandAnswer(discordEmbedMessages);
	}

	private String getRequestedCharacter(final EventDetails eventDetails) {
		return eventDetails.getOption(CHARACTER_OPTION_NAME);
	}

	private CharacterMatches getCharacterMatches(final String requestedCharacter) {
		return characterMatchService.getMatches(requestedCharacter, REQUESTED_CHARACTER_PROXIMITY);
	}

	private List<EmbedBuilder> createDiscordEmbedMessages(final CharacterMatches matches) {
		if (matches.noMatchesFound()) {
			return noMatchesFoundMessage();
		}

		if (matches.exactMatchFound()) {
			return exactMatchFoundMessage(matches.getExactMatch());
		}

		return approximateMatchesFoundMessage(matches.getApproximateMatches());
	}

	private List<EmbedBuilder> noMatchesFoundMessage() {
		EmbedBuilder embedMessage = DiscordMessageUtility.addFooter(discordEmbedMessageBuilderFactory.getEmbedMessageBuilder(MessageType.WARNING)
				.addField("No matches found!", "Please try requesting another character", true));

		return Arrays.asList(embedMessage);
	}

	private List<EmbedBuilder> exactMatchFoundMessage(final CharacterDto characterDto) {
		return Arrays.asList(createEmbedMessage(characterDto));
	}

	private List<EmbedBuilder> approximateMatchesFoundMessage(final List<CharacterDto> approximateMatches) {
		List<EmbedBuilder> embedMessages = new ArrayList<>(approximateMatches.size());
		for (final CharacterDto approximateMatch : approximateMatches) {
			if (embedMessages.size() < MAX_EMBED_MESSAGES) {
				embedMessages.add(createEmbedMessage(approximateMatch));
			}
		}
		return embedMessages;
	}

	private EmbedBuilder createEmbedMessage(final CharacterDto characterDto) {
		final List<CharacterModsDto> characterModsDtos = retrieveCharacterMods(characterDto);

		final EmbedBuilder embedBuilder = discordEmbedMessageBuilderFactory.getEmbedMessageBuilder(MessageType.SUCCESS)
				.setAuthor(characterDto.getName(), characterDto.getUrl(), characterDto.getImageUrl())
				.setThumbnail(characterDto.getImageUrl());
		for (int i = 0; i < characterModsDtos.size(); i++) {
			final CharacterModsDto characterModsDto = characterModsDtos.get(i);
			final String title = i == FIRST_VARIANT ? CATEGORY_SEPARATOR : getModVariantTitle(i);
			embedBuilder.addField(title, characterModsDto.getModsFormatted(), true);
		}

		embedBuilder.addField("Description", characterDto.getDescription(), false);

		return DiscordMessageUtility.addFooter(embedBuilder);
	}

	private List<CharacterModsDto> retrieveCharacterMods(final CharacterDto characterDto) {
		return characterModsMapper.mapAsList(characterModsRepository.findAllById(characterDto.getBaseId()), CharacterModsDto.class);
	}

	private String getModVariantTitle(final int i) {
		return String.format(MOD_VARIANT_TITLE, i);
	}

	private CommandAnswer createCommandAnswer(final List<EmbedBuilder> discordEmbedMessages) {
		return new CommandAnswer.Builder().embedMessages(discordEmbedMessages).build();
	}
}
