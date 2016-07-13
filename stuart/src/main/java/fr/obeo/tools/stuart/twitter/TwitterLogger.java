package fr.obeo.tools.stuart.twitter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import fr.obeo.tools.stuart.Post;
import twitter4j.MediaEntity;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterLogger {

	private static final String TWITTER_ICON = "https://abs.twimg.com/favicons/favicon.ico";

	private Twitter twitter;

	public TwitterLogger(String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret) {
		super();
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setOAuthConsumerKey(consumerKey).setOAuthConsumerSecret(consumerSecret).setOAuthAccessToken(accessToken)
				.setOAuthAccessTokenSecret(accessTokenSecret);
		TwitterFactory tf = new TwitterFactory(cb.build());
		twitter = tf.getInstance();
	}

	public Collection<Post> twitterLog(Date daysAgo, Collection<String> handles) {
		List<Post> posts = new ArrayList<Post>();

		try {

			for (String twitterHandle : handles) {
				ResponseList<Status> result = twitter.getUserTimeline("bruncedric", new Paging(1));
				for (Status status : result) {
					if (status.getCreatedAt().after(daysAgo)) {
						if (status.isRetweet()
								&& !handles.contains(status.getRetweetedStatus().getUser().getScreenName())) {
							Post newPost = createPost(status);
							posts.add(newPost);
						} else if (!status.isRetweet()) {

						}

					}
				}
				System.out.println(twitter.getRateLimitStatus().toString());
			}
		} catch (TwitterException e) {
			throw new RuntimeException(e);
		}
		return posts;
	}

	public static Post createPost(Status status) {
		Post newPost = null;
		String url = "https://twitter.com/" + status.getUser().getScreenName() + "/status/" + status.getId();
		if (status.isRetweet()) {
			String iconText = "[![pic](" + status.getRetweetedStatus().getUser().getProfileImageURLHttps() + ")](" + url
					+ ")";
			newPost = Post.createPost(url, iconText + " " + status.getText(),
					status.getRetweetedStatus().getUser().getScreenName(), TWITTER_ICON, status.getCreatedAt());
		} else {
			String iconText = "[![pic](" + status.getUser().getProfileImageURLHttps() + ")](" + url + ")";
			newPost = Post.createPost(url, iconText + " " + status.getText(), status.getUser().getScreenName(),
					TWITTER_ICON, status.getCreatedAt());
		}
		for (MediaEntity entity : status.getMediaEntities()) {
			newPost.addMediaURLs(entity.getMediaURLHttps());
		}

		return newPost;
	}

}
