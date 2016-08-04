package fr.obeo.tools.stuart.mattermost.bot;

import java.io.IOException;
import java.util.List;

import b4j.core.Issue;
import fr.obeo.tools.stuart.bugzilla.BugzillaLogger;

public class BugzillaReferences implements ReactOnMessage {

	private BugzillaLogger bug;

	public BugzillaReferences(String serverURL) {
		this.bug = new BugzillaLogger(serverURL);
	}

	@Override
	public void onMessage(MMBot bot, MPost p) throws IOException {
		if (!p.isFromWebhook() && !bot.getUser().getId().equals(p.getUserId())) {
			List<Issue> issues = bug.findBugzillaIssues(p.getMessage());
			if (issues.size() > 0) {
				if (issues.size() == 1) {
					Issue req = issues.iterator().next();
					bot.respond(p, "[" + req.getSummary() + "](" + req.getUri() + ")");
				} else {
					for (StringBuffer resp : BugzillaLogger.getTableReport(issues)) {
						bot.respond(p, resp.toString());
					}
				}

			}
		}
	}

}
