package com.bobasalliance.bobasbot.commands.commands.mods;

import java.time.LocalDate;

public class CharacterDto {
	private String name;
	private String baseId;
	private String url;
	private String imageUrl;
	private int power;
	private String description;
	private LocalDate expiration;
	private String shortName;

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getBaseId() {
		return baseId;
	}

	public void setBaseId(final String baseId) {
		this.baseId = baseId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(final String url) {
		this.url = url;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(final String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public int getPower() {
		return power;
	}

	public void setPower(final int power) {
		this.power = power;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public LocalDate getExpiration() {
		return expiration;
	}

	public void setExpiration(final LocalDate expiration) {
		this.expiration = expiration;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(final String shortName) {
		this.shortName = shortName;
	}
}
