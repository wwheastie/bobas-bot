package com.bobasalliance.bobasbot.commands.api.mods;

public class CharacterModsDto {
	private static final String CHARACTER_MODS_MESSAGE = "**Set 1** : %s\n**Set 2** : %s\n**Set 3** : %s\n\n**☐** : %s | **➚** : %s\n**◆** : %s | **Δ** : %s\n**O** : %s | **✙** : %s\n ";
	private static final String OFFENSE = "Offense";
	private static final String DEFENSE = "Defense";

	private String name;
	private String shortName;
	private String baseId;
	private String set1;
	private String set2;
	private String set3;
	private String square;
	private String arrow;
	private String diamond;
	private String triangle;
	private String circle;
	private String cross;

	public CharacterModsDto() {
		this.square = OFFENSE;
		this.diamond = DEFENSE;
	}

	public String getModsFormatted() {
		return String.format(CHARACTER_MODS_MESSAGE,
				getSet1(),
				getSet2(),
				getSet3(),
				getSquare(),
				getArrow(),
				getDiamond(),
				getTriangle(),
				getCircle(),
				getCross());
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(final String shortName) {
		this.shortName = shortName;
	}

	public String getBaseId() {
		return baseId;
	}

	public void setBaseId(final String baseId) {
		this.baseId = baseId;
	}

	public String getSet1() {
		return set1;
	}

	public void setSet1(final String set1) {
		this.set1 = set1;
	}

	public String getSet2() {
		return set2;
	}

	public void setSet2(final String set2) {
		this.set2 = set2;
	}

	public String getSet3() {
		return set3;
	}

	public void setSet3(final String set3) {
		this.set3 = set3;
	}

	public String getSquare() {
		return square;
	}

	public void setSquare(final String square) {
		this.square = square;
	}

	public String getArrow() {
		return arrow;
	}

	public void setArrow(final String arrow) {
		this.arrow = arrow;
	}

	public String getDiamond() {
		return diamond;
	}

	public void setDiamond(final String diamond) {
		this.diamond = diamond;
	}

	public String getTriangle() {
		return triangle;
	}

	public void setTriangle(final String triangle) {
		this.triangle = triangle;
	}

	public String getCircle() {
		return circle;
	}

	public void setCircle(final String circle) {
		this.circle = circle;
	}

	public String getCross() {
		return cross;
	}

	public void setCross(final String cross) {
		this.cross = cross;
	}
}
