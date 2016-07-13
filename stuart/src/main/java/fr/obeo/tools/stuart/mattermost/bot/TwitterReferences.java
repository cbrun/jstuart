package fr.obeo.tools.stuart.mattermost.bot;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import fr.obeo.tools.stuart.MattermostPost;
import fr.obeo.tools.stuart.Post;
import fr.obeo.tools.stuart.twitter.TwitterLogger;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

public class TwitterReferences implements ReactOnMessage {

	private Twitter twitter;

	public TwitterReferences(Twitter twitter) {
		this.twitter = twitter;
	}

	@Override
	public void onMessage(MMBot bot, MPost p) throws IOException {
		if (!p.isFromWebhook() && !bot.getUser().getId().equals(p.getUserId())) {
			List<Status> tweets = findTweets(p.getMessage());
			if (tweets.size() > 0) {
				for (Status status2 : tweets) {
					Post tPost = TwitterLogger.createPost(status2);
					bot.respond(p, MattermostPost.fromGenericPost(tPost).getText());
				}

			}
		}
	}

	private List<Status> findTweets(String content) {

		Set<String> tweets = Sets.newLinkedHashSet();

		/*
		 * search for twitter URLs
		 * 
		 * 
		 * https://twitter.com/bruncedric/status/752933286008197120
		 * 
		 */
		Pattern patchsetURL = Pattern.compile("https://twitter.com/[\\w|\\d]+/status/(\\d+)/*");
		if (patchsetURL.matcher(content).find()) {
			Matcher matcher = patchsetURL.matcher(content);
			while (matcher.find()) {
				String found = matcher.group(1);
				tweets.add(found);
			}
		}

		List<Status> result = Lists.newArrayList();
		for (String tweetid : tweets) {
			long t = Long.valueOf(tweetid).longValue();

			try {
				ResponseList<Status> r = twitter.lookup(t);
				for (Status status : r) {
					result.add(status);
				}
			} catch (TwitterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return result;
	}

}
