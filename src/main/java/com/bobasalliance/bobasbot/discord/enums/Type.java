package com.bobasalliance.bobasbot.discord.enums;

public enum Type {
	SUB_COMMAND(1),
	STRING(3),
	INTEGER(4);

	private final int value;

	Type(final int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
