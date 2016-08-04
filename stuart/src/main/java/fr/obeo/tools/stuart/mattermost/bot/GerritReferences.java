package fr.obeo.tools.stuart.mattermost.bot;

import java.io.IOException;
import java.util.List;

import fr.obeo.tools.stuart.gerrit.GerritLogger;
import fr.obeo.tools.stuart.gerrit.model.PatchSet;
import fr.obeo.tools.stuart.git.GitLogger;

public class GerritReferences implements ReactOnMessage {

	private GerritLogger gerrit;
	private String serverURL;

	public GerritReferences(String serverURL) {
		this.gerrit = new GerritLogger(serverURL);
		this.serverURL = serverURL;
	}

	@Override
	public void onMessage(MMBot bot, MPost p) throws IOException {
		if (!p.isFromWebhook() && !bot.getUser().getId().equals(p.getUserId())) {
			List<PatchSet> patchsets = gerrit.findGerritPatchsets(p.getMessage());
			if (patchsets.size() > 0) {
				if (patchsets.size() == 1) {
					PatchSet req = patchsets.iterator().next();
					bot.respond(p, GitLogger.detectBugzillaLink(
							req.getSubject() + " ->[link](" + serverURL + "/#/c/" + req.get_number() + ")"));
				} else {
					for (StringBuffer resp : gerrit.getTableReport(patchsets)) {
						bot.respond(p, resp.toString());
					}
				}

			}
		}
	}

}
