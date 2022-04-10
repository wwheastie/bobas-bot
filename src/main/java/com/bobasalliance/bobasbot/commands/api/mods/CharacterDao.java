package com.bobasalliance.bobasbot.commands.api.mods;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("characters")
public class CharacterDao {
	@Id
	@Column("name")
	private String name;

	@Column("baseID")
	private String baseId;

	@Column("url")
	private String url;

	@Column("image")
	private String imageUrl;

	@Column("power")
	private int power;

	@Column("description")
	private String description;

	@Column("expiration")
	private LocalDate expiration;

	@Column("shortname")
	private String shortName;

	public String getName() {
		return name;
	}

	public String getBaseId() {
		return baseId;
	}

	public String getUrl() {
		return url;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public int getPower() {
		return power;
	}

	public String getDescription() {
		return description;
	}

	public LocalDate getExpiration() {
		return expiration;
	}

	public String getShortName() {
		return shortName;
	}
}
