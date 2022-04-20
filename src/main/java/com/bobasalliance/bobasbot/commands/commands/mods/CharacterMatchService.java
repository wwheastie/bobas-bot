package com.bobasalliance.bobasbot.commands.commands.mods;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.similarity.JaroWinklerDistance;
import org.springframework.stereotype.Component;

@Component
public class CharacterMatchService {
	private static final double EXACT_MATCH_DISTANCE = 1.0;
	private static final int MIN_REQUIRED_REQUESTED_NAME_LENGTH = 3;

	private final CharacterRepository characterRepository;
	private final JaroWinklerDistance jaroWinklerDistance;
	private final CharacterMapper characterMapper;

	@Inject
	public CharacterMatchService(final CharacterRepository characterRepository,
			final JaroWinklerDistance jaroWinklerDistance, final CharacterMapper characterMapper) {
		this.characterRepository = characterRepository;
		this.jaroWinklerDistance = jaroWinklerDistance;
		this.characterMapper = characterMapper;
	}

	public CharacterMatches getMatches(final String requestedCharacter, final double proximity) {
		// TODO: Update character & character mods tables (ids and names) must be updated (potential bug with this logic)
		final List<CharacterDto> characterDtos = retrieveAllCharacters();

		List<CharacterDto> approximateMatches = new ArrayList<>();
		CharacterDto exactMatch = null;

		for (final CharacterDto characterDto : characterDtos) {
			final String shortName = StringUtils.isNotEmpty(characterDto.getShortName()) ? characterDto.getShortName() : StringUtils.EMPTY;
			final String name = StringUtils.isNotEmpty(characterDto.getName()) ? characterDto.getName() : StringUtils.EMPTY;

			double shortNameDistance = jaroWinklerDistance.apply(requestedCharacter, shortName);
			double nameDistance = jaroWinklerDistance.apply(requestedCharacter, name);

			if (exactMatchFound(shortNameDistance, shortName, requestedCharacter) ||
					exactMatchFound(nameDistance, name, requestedCharacter)) {
				exactMatch = characterDto;
				approximateMatches.clear();
				break;
			} else if (approximateMatchFound(shortNameDistance, proximity) ||
					approximateMatchFound(nameDistance, proximity) ||
					containsRequestedName(characterDto, requestedCharacter)) {
				approximateMatches.add(characterDto);
			}
		}

		return createCharacterMatches(exactMatch, approximateMatches);
	}

	private List<CharacterDto> retrieveAllCharacters() {
		return characterMapper.mapAsList(characterRepository.findAll(), CharacterDto.class);
	}

	private boolean exactMatchFound(final double distance, final String currentName, final String requestCharacter) {
		return EXACT_MATCH_DISTANCE == distance ||
				currentName.toLowerCase(Locale.ROOT).equals(requestCharacter.toLowerCase(Locale.ROOT));
	}

	private boolean approximateMatchFound(final double distance, final double requestProximity) {
		return distance >= requestProximity;
	}

	private boolean containsRequestedName(final CharacterDto characterDto, final String requestedCharacter) {
		return requestedCharacter.length() >= MIN_REQUIRED_REQUESTED_NAME_LENGTH &&
				characterDto.getName().toLowerCase(Locale.ROOT).contains(requestedCharacter.toLowerCase(Locale.ROOT));
	}

	private CharacterMatches createCharacterMatches(final CharacterDto exactMatch, final List<CharacterDto> approximateMatches) {
		return new CharacterMatches.Builder()
				.exactMatch(exactMatch)
				.approximateMatches(approximateMatches)
				.build();
	}
}
