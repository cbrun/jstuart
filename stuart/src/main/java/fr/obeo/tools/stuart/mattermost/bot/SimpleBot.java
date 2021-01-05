package fr.obeo.tools.stuart.mattermost.bot;

import java.io.IOException;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.OptionHandlerFilter;
import org.kohsuke.args4j.spi.StringArrayOptionHandler;

import com.squareup.okhttp.OkHttpClient;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class SimpleBot {

	@Option(required = true, name = "-url", usage = "url of the mattermost server to connect to.")
	private String mattermostServer;

	@Option(required = true, name = "-login", usage = "login to use to connect to the mattermost server.")
	private String login;

	@Option(required = true, name = "-password", usage = "password to use to connect to the mattermost server.")
	private String pwd;

	@Option(name = "-bugzillas", usage = "urls of bugzilla servers.", handler = StringArrayOptionHandler.class)
	private String[] bugzillas = new String[0];

	@Option(name = "-gerrits", usage = "urls of gerrit servers.", handler = StringArrayOptionHandler.class)
	private String[] gerrits = new String[0];

	@Option(name = "-twitter-consumer-key", usage = "The consummer key to hit the twitter APIs.")
	private String twitterConsumerKey = null;

	@Option(name = "-twitter-consumer-secret", usage = "The consummer secret to hit the twitter APIs.")
	private String twitterConsumerSecret = null;

	@Option(name = "-twitter-access-token", usage = "The access token to hit the twitter APIs.")
	private String twitterAccessToken = null;

	@Option(name = "-twitter-access-token-secret", usage = "The access token secret to hit the twitter APIs.")
	private String twitterAccessTokenSecret = null;

	@Option(name = "-shared-tasks-sheet-id", usage = "The ID of the Google Spreadsheet used for the shared tasks, e.g. https://docs.google.com/spreadsheets/d/XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX/edit#gid=0")
	private String sharedTasksSheetId = null;

	public void doMain(String[] args) throws IOException {

		CmdLineParser parser = new CmdLineParser(this);

		// if you have a wider console, you could increase the value;
		// here 80 is also the default
		parser.setUsageWidth(80);

		try {
			// parse the arguments.
			parser.parseArgument(args);

		} catch (CmdLineException e) {
			// if there's a problem in the command line,
			// you'll get this exception. this will report
			// an error message.
			System.err.println(e.getMessage());
			System.err.println("java SimpleBot [options...] arguments...");
			// print the list of available options
			parser.printUsage(System.err);
			System.err.println();

			// print option sample. This is useful some time
			System.err.println("  Example: java SampleMain" + parser.printExample(OptionHandlerFilter.REQUIRED));

			return;
		}

		OkHttpClient client = new OkHttpClient();
		final MMBot bot = MMBot.logIn(client, this.mattermostServer, this.login, this.pwd);

		bot.onMessage(new RespondForSharedTasks(sharedTasksSheetId));

		// TODO: uncommented while we develop.
//		for (String bugzilla : bugzillas) {
//			bot.onMessage(new RespondWithBugzillaReferences(bugzilla));
//		}
//
//		for (String gerrit : gerrits) {
//			bot.onMessage(new RespondWithGerritReferences(gerrit));
//		}
//
//		if (twitterAccessToken != null && twitterAccessTokenSecret != null && twitterConsumerKey != null
//				&& twitterConsumerSecret != null) {
//			ConfigurationBuilder cb = new ConfigurationBuilder();
//
//			cb.setOAuthConsumerKey(this.twitterConsumerKey).setOAuthConsumerSecret(this.twitterConsumerSecret)
//					.setOAuthAccessToken(this.twitterAccessToken)
//					.setOAuthAccessTokenSecret(this.twitterAccessTokenSecret);
//			TwitterFactory tf = new TwitterFactory(cb.build());
//			Twitter twitter = tf.getInstance();
//			bot.onMessage(new RespondWithTwitterReferences(twitter));
//		}
//		
//		bot.onMessage(new RespondWithGiphyAnimation());
//		bot.onMessage(new RespondWithPullRequestsReferences());
//		bot.onMessage(new RespondSelfDiagnostic());
		////

		bot.listen();

	}

	public static void main(String[] args) {

		try {
			new SimpleBot().doMain(args);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
