package com.bobasalliance.bobasbot.commands.api;

import com.vdurmont.emoji.Emoji;
import com.vdurmont.emoji.EmojiManager;

public class ReactionEmojis {
	private static final String WHITE_CHECK_MARK_ALIAS = "white_check_mark";
	private static final String X_ALIAS = "x";

	public static Emoji getOk() {
		return EmojiManager.getForAlias(WHITE_CHECK_MARK_ALIAS);
	}

	public static Emoji getCancel() {
		return EmojiManager.getForAlias(X_ALIAS);
	}

	private ReactionEmojis() {}
}
