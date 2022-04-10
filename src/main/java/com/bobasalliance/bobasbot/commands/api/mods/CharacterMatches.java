package com.bobasalliance.bobasbot.commands.api.mods;

import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

public class CharacterMatches {
	private final CharacterDto exactMatch;
	private final List<CharacterDto> approximateMatches;

	private CharacterMatches(final Builder builder) {
		this.exactMatch = builder.exactMatch;
		this.approximateMatches = CollectionUtils.isNotEmpty(builder.approximateMatches) ? builder.approximateMatches : Collections.emptyList();
	}

	public CharacterDto getExactMatch() {
		return exactMatch;
	}

	public List<CharacterDto> getApproximateMatches() {
		return approximateMatches;
	}

	public boolean exactMatchFound() {
		return exactMatch != null;
	}

	public boolean noMatchesFound() {
		return exactMatch == null && CollectionUtils.isEmpty(approximateMatches);
	}

	public static class Builder {
		private CharacterDto exactMatch;
		private List<CharacterDto> approximateMatches;

		public Builder exactMatch(final CharacterDto exactMatch) {
			this.exactMatch = exactMatch;
			return this;
		}

		public Builder approximateMatches(final List<CharacterDto> approximateMatches) {
			this.approximateMatches = approximateMatches;
			return this;
		}

		public CharacterMatches build() {
			return new CharacterMatches(this);
		}
	}
}
