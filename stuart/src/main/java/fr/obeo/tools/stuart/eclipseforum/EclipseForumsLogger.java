package fr.obeo.tools.stuart.eclipseforum;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.safety.Whitelist;

import com.google.common.base.Splitter;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import fr.obeo.tools.stuart.Post;

public class EclipseForumsLogger {

	private static final int MAX_REQUESTS = 100000;
	private static final String FORUM_ICON = "https://billing.ragesw.com/images/forum_speachbubble.png";
	private int forumNumber;
	private Date daysAgo;

	private boolean silentFail = true;

	private String baseURL = "http://www.eclipse.org/forums/";

	public EclipseForumsLogger(int forumNumber, Date daysAgo) {
		super();
		this.forumNumber = forumNumber;
		this.daysAgo = daysAgo;
	}

	public EclipseForumsLogger setBaseURL(String newURL) {
		this.baseURL = newURL;
		return this;
	}

	public Collection<Post> forumLog() {
		List<Post> posts = new ArrayList<Post>();
		int requestSize = 50;
		SyndFeedInput input = new SyndFeedInput();
		boolean foundAnOld = false;
		int nbSuccessiveFailures = 0;
		for (int i = 0; i < MAX_REQUESTS && !foundAnOld && nbSuccessiveFailures < 5; i++) {
			URL feedUrl = null;
			try {
				feedUrl = new URL(baseURL + "feed.php?mode=m&l=1&basic=1&frm=" + forumNumber + "&n=" + requestSize
						+ "&o=" + i * requestSize);

				System.out.println(feedUrl);
				SyndFeed feed = input.build(new XmlReader(feedUrl));
				for (SyndEntry entry : feed.getEntries()) {
					if (entry.getPublishedDate().after(daysAgo)) {
						String html = entry.getDescription().getValue();
						Document doc = Jsoup.parse(html);
						doc.getElementsByClass("pre").remove();
						String htmlConvertedToText = Jsoup.clean(doc.toString(), "", Whitelist.none(),
								new OutputSettings().prettyPrint(false));
						StringBuffer cleaned = new StringBuffer();
						boolean foundSignature = false;
						for (String line : Splitter.on('\n').split(htmlConvertedToText)) {
							if (line.trim().length() > 0 && !line.startsWith("&gt;") && !foundSignature) {
								if (line.startsWith("--")) {
									foundSignature = true;
								} else {
									cleaned.append(line);
									cleaned.append('\n');
								}
							}
						}

						Post newPost = Post
								.createPostWithSubject(entry.getUri(), entry.getTitle(), cleaned.toString(),
										entry.getAuthor(), FORUM_ICON, entry.getPublishedDate())
								.addURLs(entry.getUri());
						Element img = Jsoup.parse(html).getElementsByTag("img").first();
						if (img != null && img.attr("src") != null) {
							String href = img.attr("src");
							if (!href.startsWith("http")) {
								href = "https://www.eclipse.org/forums/" + href;
							}
							newPost.addMediaURLs(href);
						}
						posts.add(newPost);
					} else {
						foundAnOld = true;
					}

				}
				nbSuccessiveFailures = 0;
			} catch (MalformedURLException e) {
				System.err.println(feedUrl + " " + e.getMessage());
				nbSuccessiveFailures++;
				if (!silentFail) {
					throw new RuntimeException(e);
				}
			} catch (IllegalArgumentException e) {
				System.err.println(feedUrl + " " + e.getMessage());
				nbSuccessiveFailures++;
				if (!silentFail) {
					throw new RuntimeException(e);
				}
			} catch (FeedException e) {
				System.err.println(feedUrl + " " + e.getMessage());
				nbSuccessiveFailures++;
				if (!silentFail) {
					throw new RuntimeException(e);
				}
			} catch (IOException e) {
				System.err.println(feedUrl + " " + e.getMessage());
				nbSuccessiveFailures++;
				if (!silentFail) {
					throw new RuntimeException(e);
				}
			}
		}
		/*
		 * set the thread ID
		 */
		for (Post post : posts) {
			post.setThreadID(findThread(post));
		}
		return posts;
	}

	private static String findThread(Post post) {
		String id = "";
		String url = post.getKey();
		String threadAndMessId = url.substring(url.indexOf("msg/") + 4);
		id = threadAndMessId.substring(0, threadAndMessId.indexOf("/"));
		return id;
	}

	public EclipseForumsLogger setSilentFail(boolean val) {
		this.silentFail = val;
		return this;
	}

}
