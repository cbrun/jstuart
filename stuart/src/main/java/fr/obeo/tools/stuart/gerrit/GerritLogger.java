package fr.obeo.tools.stuart.gerrit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
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

	public GerritLogger(String serverURL) {
		this.serverURL = serverURL;
		gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").setPrettyPrinting().create();
	}

	public Collection<Post> getPatchsets(int nbDays, Collection<String> projects) {
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
			for (PatchSet review : recentReviews) {
				if (review.isMergeable() && !review.getSubject().contains("DRAFT")) {
					Post newPost = Post.createPostWithSubject(serverURL + "/" + review.getId(),
							GitLogger.detectBugzillaLink(review.getSubject()), "ready for review (Validated and mergeable)\n lines +"
									+ review.getInsertions() + ", -" + review.getDeletions(),
							review.getOwner().getName(), GERRIT_ICON,review.getUpdated());
					newPost.addURLs(serverURL + "/#/c/" + review.get_number());
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
