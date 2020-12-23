package fr.obeo.tools.stuart.mattermost.bot;

import java.io.IOException;

public class RespondForSharedTasksBotChannel implements ReactOnMessage {

	public RespondForSharedTasksBotChannel() {
	}

	@Override
	public void onMessage(MMBot bot, MPost p) throws IOException {
		if (!p.isFromWebhook() && isNotFromBotOrIsDiagnostic(bot, p)) {
			if (p.getChannelId().equals("whxbgbtqrtr5jetdri7o9bxqiw")) {
				String message = "Vous avez écrit ce message:" + p.getMessage() ;
				bot.respond(p, message);
			}
		}
	}
}
