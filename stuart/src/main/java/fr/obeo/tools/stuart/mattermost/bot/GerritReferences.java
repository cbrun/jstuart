package fr.obeo.tools.stuart.mattermost.bot;

import java.io.IOException;
import java.util.List;

import fr.obeo.tools.stuart.gerrit.GerritLogger;
import fr.obeo.tools.stuart.gerrit.model.PatchSet;

public class GerritReferences implements ReactOnMessage {

	private GerritLogger gerrit;

	public GerritReferences(String serverURL) {
		this.gerrit = new GerritLogger(serverURL);
	}

	@Override
	public void onMessage(MMBot bot, MPost p) throws IOException {
		if (!p.isFromWebhook() && !bot.getUser().getId().equals(p.getUserId())) {
			List<PatchSet> patchsets = gerrit.findGerritPatchsets(p.getMessage());
			if (patchsets.size() > 0) {

				for (StringBuffer resp : gerrit.getTableReport(patchsets)) {
					bot.respond(p, resp.toString());
				}

			}
		}
	}

}
