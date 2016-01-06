package fr.obeo.tools.stuart.jenkins;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import fr.obeo.tools.stuart.Post;
import fr.obeo.tools.stuart.jenkins.model.BuildAction;
import fr.obeo.tools.stuart.jenkins.model.BuildAuthor;
import fr.obeo.tools.stuart.jenkins.model.BuildCause;
import fr.obeo.tools.stuart.jenkins.model.BuildResult;
import fr.obeo.tools.stuart.jenkins.model.ChangeSetItem;
import fr.obeo.tools.stuart.jenkins.model.Job;
import fr.obeo.tools.stuart.jenkins.model.ServerResult;

public class JenkinsLogger {

	private static final String JENKINS_ICON = "https://www.urbaninsight.com/files/pkg.png";

	private String serverURL;
	private Gson gson;

	private Date daysAgo;

	public JenkinsLogger(String serverURL, Date daysAgo) {
		this.serverURL = serverURL;
		gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").setPrettyPrinting().create();
		this.daysAgo = daysAgo;
	}

	public Collection<Post> getBuildResults() {
		List<Post> posts = new ArrayList<Post>();
		String url = serverURL + "api/json?tree=jobs[name,lastBuild[building,timestamp,url]]";
		Request request = new Request.Builder().url(url).get().build();
		OkHttpClient client = new OkHttpClient();
		Response response = null;
		try {
			response = client.newCall(request).execute();
			String returnedJson = response.body().string();
			ServerResult recentReviews = gson.fromJson(returnedJson, ServerResult.class);
			for (Job j : recentReviews.getJobs()) {
				if (j.getLastBuild() != null && !j.getLastBuild().getUrl().contains("gerrit")) {
					String jURL = j.getLastBuild().getUrl() + "/api/json";

					Date lastJobTime = new Date(j.getLastBuild().getTimestamp());
					if (lastJobTime.after(daysAgo)) {

						Response jobResponse = client.newCall(new Request.Builder().url(jURL).get().build()).execute();
						String jobJson = jobResponse.body().string();
						BuildResult lastBuild = gson.fromJson(jobJson, BuildResult.class);
						boolean manualTrigger = false;
						int totalCount = 0;
						int failCount = 0;
						int skipCount = 0;

						String testsResults = "";

						for (BuildAction action : lastBuild.getActions()) {
							totalCount += action.getTotalCount();
							failCount += action.getFailCount();
							skipCount += action.getSkipCount();
							for (BuildCause cause : action.getCauses()) {
								if (cause.getShortDescription() != null) {
									manualTrigger = cause.getShortDescription().contains("user")
											|| cause.getShortDescription().contains("utilisateur");
								}

							}
						}

						if (totalCount > 0) {
							testsResults = " tests - total : " + totalCount + ", skipped " + skipCount + " : failed "
									+ failCount;
						}

						Collection<String> authors = Sets.newLinkedHashSet();
						for (BuildAuthor culprit : lastBuild.getCulprits()) {
							if (culprit.getFullName() != null) {
								authors.add(culprit.getFullName());
							}
						}

						Collection<String> comments = Sets.newLinkedHashSet();
						for (ChangeSetItem i : lastBuild.getChangeSet().getItems()) {
							if (i.getComment() != null) {
								comments.add(Iterables.getFirst(Splitter.on('\n').split(i.getComment()), "no commit"));
							}
						}
						String authorTxt = "...";
						if (authors.size() <= 2 && authors.size() > 0) {
							authorTxt = Joiner.on(',').join(authors);
						}
						if (manualTrigger) {
							Post newPost = Post.createPostWithSubject(lastBuild.getUrl(),
									lastBuild.getFullDisplayName() + " is " + lastBuild.getResult() + testsResults,
									Joiner.on('\n').join(comments), authorTxt, JENKINS_ICON,new Date(lastBuild.getTimestamp()));
							newPost.addURLs(lastBuild.getUrl());
							posts.add(newPost);
						}
					}
				}
			}

			response.body().close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return posts;
	}

}
