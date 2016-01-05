package fr.obeo.tools.stuart.bugzilla;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import b4j.core.Comment;
import b4j.core.DefaultIssue;
import b4j.core.DefaultSearchData;
import b4j.core.Issue;
import b4j.core.session.BugzillaHttpSession;
import fr.obeo.tools.stuart.Post;

public class BugzillaLogger {
	private static final String BUG_ICON = "https://cdn2.iconfinder.com/data/icons/windows-8-metro-style/128/bug.png";
	private String baseURL = "https://bugs.eclipse.org/bugs";
	private Set<String> authorsToIgnore;

	public BugzillaLogger(String baseURL, Set<String> authorsToIgnore) {
		this.baseURL = baseURL;
		this.authorsToIgnore = authorsToIgnore;
	}

	public Collection<Post> bugzillaLog(int nbDaysAgo, Collection<String> products) throws MalformedURLException {
		List<Post> posts = new ArrayList<Post>();
		BugzillaHttpSession session = new BugzillaHttpSession();
		session.setBaseUrl(new URL(this.baseURL));
		session.setBugzillaBugClass(DefaultIssue.class);
		if (session.open()) {
			DefaultSearchData searchData = new DefaultSearchData();
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -nbDaysAgo);
			Date daysAgo = cal.getTime();
			for (String productName : products) {
				searchData.add("product", productName);
			}
			searchData.add("limit", "20");

			SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
			searchData.add("chfieldfrom", dt.format(daysAgo));
			searchData.add("chfieldto", "Now");

			Iterable<Issue> returnedIssues = session.searchBugs(searchData, null);

			for (Issue issue : returnedIssues) {
				if (issue.getCommentCount() == 0) {

					if (issue.getCreationTimestamp().after(daysAgo)
							&& !this.authorsToIgnore.contains(issue.getReporter().getName())) {
						posts.add(Post.createPostWithSubject(issue.getUri(),
								"[[" + issue.getId() + "](" + issue.getUri() + ")] " +  issue.getSummary(),
								"Has been created", issue.getReporter().getName(), BUG_ICON,issue.getCreationTimestamp()).addURLs(issue.getUri()));
					}

				} else {
					int index = 0;
					for (Comment c : issue.getComments()) {
						if (!authorsToIgnore.contains(c.getAuthor().getName())) {
							if (c.getCreationTimestamp().after(daysAgo)) {
								String commentURL = issue.getUri() + "#c" + index;
								posts.add(Post
										.createPostWithSubject(commentURL, "[[" + issue.getId() + "](" + commentURL + ")] " +  issue.getSummary(),
												c.getTheText(), c.getAuthor().getName(), BUG_ICON,c.getCreationTimestamp())
										.addURLs(commentURL));
							}
						}
						index++;
					}
				}
			}

			session.close();
		}
		return posts;
	}
}
