package fr.obeo.tools.stuart.mattermost.bot;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import fr.obeo.tools.stuart.MattermostPost;
import fr.obeo.tools.stuart.Post;
import fr.obeo.tools.stuart.twitter.TwitterLogger;
import twitter4j.Status;

public class RespondWithPullRequestsReferences implements ReactOnMessage {

	public RespondWithPullRequestsReferences() {
	}

	@Override
	public void onMessage(MMBot bot, MPost p) throws IOException {
		if (!p.isFromWebhook() && isNotFromBotOrIsDiagnostic(bot, p)) {

			List<String> pullRequests = findPullRequests(p.getMessage());
			if (pullRequests.size() > 0) {
				for (String status2 : pullRequests) {
					bot.respond(p, status2);
				}

			}
		}
	}

	private List<String> findPullRequests(String content) {

		Set<String> pull = Sets.newLinkedHashSet();

		/*
		 * search for pull request URLs
		 * 
		 * 
		 * https://github.com/ObeoNetwork/M2Doc/pull/89
		 * 
		 */
		Pattern patchsetURL = Pattern.compile("(https://github.com/[\\w|\\d]+/[\\w|\\d]+/pull/(\\d+)*)");
		if (patchsetURL.matcher(content).find()) {
			Matcher matcher = patchsetURL.matcher(content);
			while (matcher.find()) {
				String url = matcher.group(1);
				String pullRequest = matcher.group(2);
				pull.add(url);
			}
		}

		List<String> result = Lists.newArrayList();
		for (String pullRequestURL : pull) {
			try {
				Document doc = Jsoup.connect(pullRequestURL).get();
				String title = doc.select("span.js-issue-title").text();
				String state = doc.select("div.state").text();
				String number = doc.select("span.gh-header-number").text();
				String diffstat = doc.select("span.diffstat").text();
				String checks = doc.select("h4.status-heading").text();
				String msg = " [" + number + " " + title + "](" + pullRequestURL + ") *" + diffstat + "*" + "   " + state + ""; 
				if (checks != null && checks.trim().length() > 0) {
					msg += "\n **" + checks + "**";
				}

				result.add(msg);
				System.out.println("RespondWithPullRequestsReferences.findPullRequests() " + pullRequestURL);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return result;
	}

}
