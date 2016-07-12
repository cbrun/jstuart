package fr.obeo.tools.stuart.bugzilla;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import b4j.core.Comment;
import b4j.core.DefaultIssue;
import b4j.core.DefaultSearchData;
import b4j.core.Issue;
import b4j.core.session.BugzillaHttpSession;
import fr.obeo.tools.stuart.Dates;
import fr.obeo.tools.stuart.Post;

public class BugzillaLogger {
	private static final String BUG_ICON = "https://cdn2.iconfinder.com/data/icons/windows-8-metro-style/128/bug.png";
	private String baseURL = "https://bugs.eclipse.org/bugs";
	private Set<String> authorsToIgnore = Sets.newLinkedHashSet();

	public BugzillaLogger() {
	}

	public BugzillaLogger(String baseURL) {
		this.baseURL = baseURL;
	}

	public BugzillaLogger(String baseURL, Set<String> authorsToIgnore) {
		this.baseURL = baseURL;
		this.authorsToIgnore = authorsToIgnore;
	}

	public Collection<Post> bugzillaLog(int nbDaysAgo, Collection<String> products) throws MalformedURLException {
		return bugzillaLog(nbDaysAgo, products, Collections.EMPTY_SET, Collections.EMPTY_SET);
	}

	public Collection<Post> bugzillaLog(int nbDaysAgo, Collection<String> products, Collection<String> components)
			throws MalformedURLException {
		return bugzillaLog(nbDaysAgo, products, components, Collections.EMPTY_SET);
	}

	public Issue find(String key) {
		Issue found = null;
		try {
			BugzillaHttpSession session = new BugzillaHttpSession();
			session.setBaseUrl(new URL(this.baseURL));
			session.setBugzillaBugClass(DefaultIssue.class);

			if (session.open()) {
				found = session.getIssue(key);
			}

			session.close();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return found;
	}

	public Collection<Post> bugzillaLog(int nbDaysAgo, Collection<String> products, Collection<String> components,
			Collection<String> keywords) throws MalformedURLException {
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
			for (String componentName : components) {
				searchData.add("component", componentName);
			}

			for (String keywordName : keywords) {
				searchData.add("keywords", keywordName);
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
								"[[" + issue.getId() + "](" + issue.getUri() + ")] " + issue.getSummary(),
								"Has been created", issue.getReporter().getName(), BUG_ICON,
								issue.getCreationTimestamp()).addURLs(issue.getUri()));
					}

				} else {
					int index = 0;
					for (Comment c : issue.getComments()) {
						if (!authorsToIgnore.contains(c.getAuthor().getName())) {
							if (c.getCreationTimestamp().after(daysAgo)) {
								String commentURL = issue.getUri() + "#c" + index;
								posts.add(Post.createPostWithSubject(commentURL,
										"[[" + issue.getId() + "](" + commentURL + ")] " + issue.getSummary(),
										c.getTheText(), c.getAuthor().getName(), BUG_ICON, c.getCreationTimestamp())
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

	public Collection<String> findPotentialBugzillaIds(String content) {
		Set<String> bugIds = Sets.newLinkedHashSet();

		/*
		 * search patters with Bug XXXXXX or bug YYYYYY
		 */
		Pattern bugNumber = Pattern.compile("[Bb]ug\\s+(\\d{6}+)");
		if (bugNumber.matcher(content).find()) {
			Matcher matcher = bugNumber.matcher(content);
			while (matcher.find()) {
				String found = matcher.group(1);
				bugIds.add(found);
			}
		}

		/*
		 * search for URLs
		 * 
		 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=497732
		 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=497734
		 * 
		 */
		Pattern bugURL = Pattern.compile(this.baseURL + "/show_bug.cgi\\?id=(\\d+)");
		if (bugURL.matcher(content).find()) {
			Matcher matcher = bugURL.matcher(content);
			while (matcher.find()) {
				String found = matcher.group(1);
				bugIds.add(found);
			}
		}

		Pattern bugShortURL = Pattern.compile(this.baseURL + "/(\\d+)");
		if (bugShortURL.matcher(content).find()) {
			Matcher matcher = bugShortURL.matcher(content);
			while (matcher.find()) {
				String found = matcher.group(1);
				bugIds.add(found);
			}
		}

		return bugIds;
	}

	public List<Issue> findBugzillaIssues(String content) {
		List<Issue> bugs = Lists.newArrayList();
		Collection<String> potentialIds = findPotentialBugzillaIds(content);
		if (potentialIds.size() > 0) {

			try {
				BugzillaHttpSession session = new BugzillaHttpSession();
				session.setBaseUrl(new URL(this.baseURL));
				session.setBugzillaBugClass(DefaultIssue.class);

				if (session.open()) {
					for (String key : potentialIds) {

						Issue found = session.getIssue(key);
						if (found != null && found.getReporter()!=null) {
							bugs.add(found);
						}
					}
				}

				session.close();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return bugs;
	}

	public static Collection<StringBuffer> getTableReport(List<Issue> issues) {
		List<StringBuffer> result = Lists.newArrayList();

		for (List<Issue> requests : Lists.partition(issues, 20)) {
			StringBuffer table = new StringBuffer();
			table.append("| Status | Summary | reporter | last update| \n");
			table.append("|---------------------------------------------------|\n");
			for (Issue req : requests) {
				long delay = (new Date().getTime() - req.getUpdateTimestamp().getTime()) / (1000 * 60);
				table.append("| " + req.getStatus().getName() + "   | [" + req.getSummary() + "](" + req.getUri() + ")"
						+ " | " + req.getReporter().getName() + " | " + Dates.prettyDelayFromMinutes(delay) + "  |");
				table.append("\n");
			}

			result.add(table);
		}

		return result;
	}
}
