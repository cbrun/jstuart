package fr.obeo.tools.stuart.gerrit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import fr.obeo.tools.stuart.Post;
import fr.obeo.tools.stuart.gerrit.model.PatchSet;
import fr.obeo.tools.stuart.git.GitLogger;

public class GerritLogger {

	private static final String GERRIT_ICON = "http://static1.squarespace.com/static/525e9a22e4b0fe5f668a8903/t/557afd6de4b05d594de11f63/1434123630015/checklist?format=300w";

	private String serverURL;
	private Gson gson;

	private int nbDays;

	private boolean groupReviews = true;

	public GerritLogger(String serverURL, int daysAgo) {
		this.serverURL = serverURL;
		gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").setPrettyPrinting().create();
		this.nbDays = daysAgo;
	}

	public Collection<Post> getPatchsets(Collection<String> projects) {
		List<Post> posts = new ArrayList<Post>();
		String prjString = Joiner.on(" OR ").join(Iterables.transform(projects, new Function<String, String>() {

			public String apply(String projectName) {
				return "project:" + projectName;
			}
		}));
		String query = "status:open AND label:Verified=1 AND -age:" + nbDays + "d AND (" + prjString + ")";

		String url = serverURL + "/changes/?q=" + query + "&o=DETAILED_ACCOUNTS&o=CURRENT_REVISION";
		Request request = new Request.Builder().url(url).get().build();
		OkHttpClient client = new OkHttpClient();
		Response response = null;
		try {
			response = client.newCall(request).execute();
			String returnedJson = response.body().string();
			PatchSet[] recentReviews = gson.fromJson(returnedJson, PatchSet[].class);
			List<PatchSet> reviewsToSend = Lists.newArrayList();
			for (PatchSet review : recentReviews) {
				if (!review.getSubject().contains("DRAFT")) {
					reviewsToSend.add(review);
				}
			}

			Collections.sort(reviewsToSend, new Comparator<PatchSet>() {
				public int compare(PatchSet m1, PatchSet m2) {
					return m1.getUpdated().compareTo(m2.getUpdated());
				}
			});
			if (reviewsToSend.size() == 1 || !groupReviews) {
				for (PatchSet review : reviewsToSend) {
					Post newPost = Post.createPostWithSubject(serverURL + "/" + review.getId(),
							GitLogger.detectBugzillaLink(review.getSubject()),
							"ready for review (Validated)\n\n" + "|additions| deletions|\n" + "|---------|----------|\n"
									+ "|" + review.getInsertions() + " | " + review.getDeletions() + "|",
							review.getOwner().getName(), GERRIT_ICON, review.getUpdated());
					newPost.addURLs(serverURL + "/#/c/" + review.get_number());
					newPost.setQuote(false);
					newPost.mightBeTruncated(false);
					posts.add(newPost);
				}
			} else if (reviewsToSend.size() > 1) {

				List<List<PatchSet>> partitions = Lists.partition(reviewsToSend, 14);
				for (List<PatchSet> reviews : partitions) {
					String body = "\n" + "|subject|author|changes| merge ?|   |\n"
							+ "|-------|:------:|---------|-------|------|----|\n";
					Set<String> authors = Sets.newLinkedHashSet();
					Set<String> urls = Sets.newLinkedHashSet();
					for (PatchSet review : reviews) {
						String reviewKey = serverURL + "/" + review.getId();
						String mergeable = ":boom:";
						if (review.isMergeable()) {
							mergeable = ":star:";
						}
						
						body += "| " + GitLogger.detectBugzillaLink(review.getSubject()) + " | "
								+ review.getOwner().getName() + " | *+" + review.getInsertions() + "/-"
								+ review.getDeletions() + "*|"+  mergeable +   "| [link](" + serverURL + "/#/c/" + review.get_number()
								+ ")|\n";
						authors.add(review.getOwner().getName());
						urls.add(reviewKey);
					}
					String authorName = Joiner.on(',').join(authors);
					String postKey = Joiner.on('_').join(urls);

					Post newPost = Post.createPostWithSubject(postKey, "Ready for reviews (" + reviews.size() + ")",
							body, authorName, GERRIT_ICON, reviews.iterator().next().getUpdated());
					newPost.setQuote(false);
					newPost.mightBeTruncated(false);
					posts.add(newPost);
				}

			}
			response.body().close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return posts;
	}

}
