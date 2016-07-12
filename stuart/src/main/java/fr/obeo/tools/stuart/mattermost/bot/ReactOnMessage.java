package fr.obeo.tools.stuart.mattermost.bot;

import java.io.IOException;

public interface ReactOnMessage {

	void onMessage(MMBot bot, MPost p) throws IOException;

}
