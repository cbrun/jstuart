package fr.obeo.tools.stuart.rss;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;

import com.google.common.base.Splitter;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.feed.synd.SyndFeedImpl;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import fr.obeo.tools.stuart.Post;

public class RssLogger {

	private String RSS_ICON = "https://i.imgur.com/nKoSYdn.png";
	private URL feedUrl;
	private Date daysAgo;

	public RssLogger(URL feedUrl, Date daysAgo) {
		this.feedUrl = feedUrl;
		this.daysAgo = daysAgo;
	}

	public RssLogger setIcon(String newIcon) {
		this.RSS_ICON = newIcon;
		return this;
	}

	public Collection<Post> get() {
		List<Post> posts = new ArrayList<Post>();
		SyndFeedInput input = new SyndFeedInput();
		boolean foundAnOld = false;
		for (int i = 0; i < 1 && !foundAnOld; i++) {
			try {
				SyndFeed feed = null;
				try (XmlReader xmlReader = new XmlReader(feedUrl)) {
					feed = input.build(xmlReader);
				} catch (IOException e) {
					// problem reading the stream or URL
					feed = new SyndFeedImpl();
				}
				Date publishedDate = feed.getPublishedDate();
				for (SyndEntry entry : feed.getEntries()) {
					publishedDate = entry.getPublishedDate();
					if (publishedDate == null) {
						publishedDate = entry.getUpdatedDate();
					}
					if (publishedDate != null && publishedDate.after(daysAgo)) {
						String html = entry.getDescription().getValue();

						String htmlConvertedToText = Jsoup.clean(html, "", Whitelist.none(),
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

						Post newPost = Post.createPostWithSubject(entry.getUri(), entry.getTitle(), cleaned.toString(),
								entry.getAuthor(), RSS_ICON, publishedDate).addURLs(entry.getUri());
						if (entry.getUri() != null && entry.getUri().startsWith("http")) {
							newPost.addURLs(entry.getUri());
						}
						if (entry.getLink() != null) {
							newPost.addURLs(entry.getLink());
						}
						Element img = Jsoup.parse(html).getElementsByTag("img").first();
						if (img != null && img.attr("src") != null) {
							String href = img.attr("src");
							newPost.addMediaURLs(href);
						}
						posts.add(newPost);
					} else {
						foundAnOld = true;
						System.out.println("Too old :" + entry.getLink());
					}

				}
			} catch (IllegalArgumentException e) {
				throw new RuntimeException(e);
			} catch (FeedException e) {
				throw new RuntimeException(e);
			}
		}
		return posts;
	}

}
