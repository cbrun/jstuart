package fr.obeo.tools.stuart.mattermost.bot;

import java.io.IOException;

public interface ReactOnMessage {

	void onMessage(MMBot bot, MPost p) throws IOException;

	public default boolean isNotFromBotOrIsDiagnostic(MMBot bot, MPost p) {
		return !bot.getUser().getId().equals(p.getUserId()) || p.getMessage().contains("!diagnose");
	}
}
