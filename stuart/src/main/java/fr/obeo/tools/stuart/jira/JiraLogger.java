package fr.obeo.tools.stuart.jira;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClientFactory;
import com.atlassian.jira.rest.client.api.domain.Comment;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.SearchResult;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

import fr.obeo.tools.stuart.Post;

public class JiraLogger {
	private static final String BUG_ICON = "https://cdn2.iconfinder.com/data/icons/windows-8-metro-style/128/bug.png";
	private String baseURL = "https://support.jira.obeo.fr/";

	private String username;

	private String password;

	public JiraLogger() {
	}

	public JiraLogger(String baseURL) {
		this.baseURL = baseURL;
	}

	public void setAuth(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public static void main(String[] args) {
		try {
			new JiraLogger().jiraLog(5, Sets.newHashSet("VP", "OD"));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Collection<Post> jiraLog(int nbDaysAgo, Collection<String> projects) throws MalformedURLException {
		List<Post> posts = new ArrayList<Post>();
		JiraRestClient jira = null;
		try {
			JiraRestClientFactory restClientFactory = new AsynchronousJiraRestClientFactory();
			URI uri = new URI(baseURL);
			if (username != null && password != null) {
				jira = restClientFactory.createWithBasicHttpAuthentication(uri, username, password);
			} else {
				jira = restClientFactory.createWithBasicHttpAuthentication(uri, "anonymous", "nopass");
			}
			jira.getProjectClient().getAllProjects().get().forEach(x -> {
				System.out.println(x.getKey());
			});

			String projectsQL = "("
					+ Joiner.on(" OR ").join(Iterables.transform(projects, new Function<String, String>() {

						@Override
						public String apply(String input) {
							return "project=" + input;
						}
					})) + " )";
			String jql = projectsQL + " AND updated>=-" + nbDaysAgo + "d ORDER BY updated DESC";
			SearchResult r = jira.getSearchClient().searchJql(jql).get();
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -nbDaysAgo);
			Date daysAgo = cal.getTime();
			for (Issue lightIssue : r.getIssues()) {
				/*
				 * for some reasons I don't get comments when using the search
				 * API and has to retrieve each issue explicitely.
				 */
				Issue i = jira.getIssueClient().getIssue(lightIssue.getKey()).get();
				String issueURL = baseURL + "browse/" + i.getKey();

				if (!i.getComments().iterator().hasNext()) {
					if (i.getCreationDate().toDate().after(daysAgo)) {
						posts.add(Post.createPostWithSubject(issueURL,
								"[[" + i.getKey() + "](" + i.getSelf() + ")] " + i.getSummary(), "Has been created",
								i.getReporter().getName(), BUG_ICON, i.getCreationDate().toDate()).addURLs(issueURL));
					}
				} else {
					for (Comment c : i.getComments()) {
						if (c.getCreationDate().toDate().after(daysAgo)) {
							String commentURL = issueURL + "?focusedCommentId=" + c.getId();
							posts.add(Post
									.createPostWithSubject(commentURL,
											"[[" + i.getKey() + "](" + commentURL + ")] " + i.getSummary(), c.getBody(),
											c.getAuthor().getName(), BUG_ICON, c.getCreationDate().toDate())
									.addURLs(commentURL));
						}
					}
				}
			}

		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (jira != null) {
				try {
					jira.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return posts;
	}

}
