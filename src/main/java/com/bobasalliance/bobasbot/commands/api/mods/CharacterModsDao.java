package com.bobasalliance.bobasbot.commands.api.mods;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("charactermods")
public class CharacterModsDao {
	@Id
	@Column("cmname")
	private String name;

	@Column("cmshortname")
	private String shortName;

	@Column("baseID")
	private String baseId;

	@Column("set1")
	private String set1;

	@Column("set2")
	private String set2;

	@Column("set3")
	private String set3;

	@Column("arrow")
	private String arrow;

	@Column("triangle")
	private String triangle;

	@Column("circle")
	private String circle;

	@Column("cross")
	private String cross;

	public String getName() {
		return name;
	}

	public String getShortName() {
		return shortName;
	}

	public String getBaseId() {
		return baseId;
	}

	public String getSet1() {
		return set1;
	}

	public String getSet2() {
		return set2;
	}

	public String getSet3() {
		return set3;
	}

	public String getArrow() {
		return arrow;
	}

	public String getTriangle() {
		return triangle;
	}

	public String getCircle() {
		return circle;
	}

	public String getCross() {
		return cross;
	}
}
