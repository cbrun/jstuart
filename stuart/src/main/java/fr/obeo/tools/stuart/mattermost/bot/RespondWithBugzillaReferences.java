package fr.obeo.tools.stuart.mattermost.bot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import b4j.core.Issue;
import fr.obeo.tools.stuart.bugzilla.BugzillaLogger;
import fr.obeo.tools.stuart.bugzilla.BugzillaLogger.CommentWithIssue;

public class RespondWithBugzillaReferences implements ReactOnMessage {

	private BugzillaLogger bug;

	public RespondWithBugzillaReferences(String serverURL) {
		this.bug = new BugzillaLogger(serverURL);
	}

	@Override
	public void onMessage(MMBot bot, MPost p) throws IOException {
		if (!p.isFromWebhook() && !bot.getUser().getId().equals(p.getUserId())) {
			List<Issue> issues = new ArrayList<Issue>();
			List<CommentWithIssue> comments = new ArrayList<CommentWithIssue>();

			bug.findBugzillaIssues(p.getMessage(), issues, comments);
			if (issues.size() > 0 || comments.size() > 0) {
				if (issues.size() == 1) {
					Issue req = issues.iterator().next();
					bot.respond(p, "[" + req.getSummary() + "](" + req.getUri() + ")");
				} else {
					for (StringBuffer resp : BugzillaLogger.getTableReport(issues)) {
						bot.respond(p, resp.toString());
					}
				}
				String c = "";
				for (CommentWithIssue comment : comments) {
					c += "> " + comment.c.getTheText() + "\n[-> " + comment.i.getSummary() + "](" + comment.i.getUri()
							+ "#c" + comment.commentNumber + ")\n";
				}
				if (comments.size() > 0) {
					bot.respond(p, c);
				}

			}
		}
	}
}
