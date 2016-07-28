package fr.obeo.tools.stuart.gerrit;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import fr.obeo.tools.stuart.Dates;
import fr.obeo.tools.stuart.Post;
import fr.obeo.tools.stuart.gerrit.model.PatchSet;
import fr.obeo.tools.stuart.git.GitLogger;

public class GerritLogger {

	private static final String GERRIT_ICON = "http://static1.squarespace.com/static/525e9a22e4b0fe5f668a8903/t/557afd6de4b05d594de11f63/1434123630015/checklist?format=300w";

	private String serverURL;
	private Gson gson;

	private boolean groupReviews = true;

	private String username;

	private String password;

	public void setAuthInfo(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public GerritLogger(String serverURL) {
		this.serverURL = serverURL;
		gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").setPrettyPrinting().create();
	}

	public Collection<Post> getPatchsets(Collection<String> projects, int nbDays) {
		List<Post> posts = new ArrayList<Post>();
		String prjString = Joiner.on(" OR ").join(Iterables.transform(projects, new Function<String, String>() {

			public String apply(String projectName) {
				return "project:" + projectName;
			}
		}));
		String query = "status:open AND label:Verified=1 AND -age:" + nbDays + "d AND (" + prjString + ")";

		String urlPath = "/changes/?q=" + query + "&o=DETAILED_ACCOUNTS&o=CURRENT_REVISION";
		String url = serverURL + urlPath;
		OkHttpClient client = new OkHttpClient();
		if (this.username != null && this.password != null) {

			/*
			 * gerrit rest api uses a prefix in case of authenticated request.
			 */
			url = serverURL + "/a" + urlPath;
			CookieManager cookieManager = new CookieManager();
			cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
			client.setCookieHandler(cookieManager);
			Response authResp;
			try {
				RequestBody body = new FormEncodingBuilder().add("username", username).add("password", password)
						.build();
				authResp = client.newCall(new Request.Builder().url(serverURL + "/login/").post(body).build())
						.execute();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		Request request = new Request.Builder().url(url).get().build();
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
							"ready for review (Validated)\n\n" + "|project|additions| deletions| mergeable | |\n"
									+ "|-----|---------|----------|----------|-----|\n" + "|" + review.getProject()
									+ "|" + review.getInsertions() + " | " + review.getDeletions() + "|"
									+ mergeableText(review) + "| [link](" + serverURL + "/#/c/" + review.get_number()
									+ ")|",
							review.getOwner().getName(), GERRIT_ICON, review.getUpdated());
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

						boolean recentlyUpdated = (new Date().getTime() - review.getUpdated().getTime()) < 24 * 60 * 60
								* 1000;
						String reviewKey = serverURL + "/" + review.getId();
						String mergeable = mergeableText(review);

						if (recentlyUpdated) {
							body += "| **" + GitLogger.detectBugzillaLink(review.getSubject()) + "** | **"
									+ review.getOwner().getName() + "** | *+" + review.getInsertions() + "/-"
									+ review.getDeletions() + "*|" + mergeable + "| [link](" + serverURL + "/#/c/"
									+ review.get_number() + ")|\n";
						} else {
							body += "| " + GitLogger.detectBugzillaLink(review.getSubject()) + " | "
									+ review.getOwner().getName() + " | *+" + review.getInsertions() + "/-"
									+ review.getDeletions() + "*|" + mergeable + "| [link](" + serverURL + "/#/c/"
									+ review.get_number() + ")|\n";
						}
						authors.add(review.getOwner().getName());
						urls.add(reviewKey);
					}
					String authorName = Joiner.on(',').join(authors);
					String postKey = Joiner.on('_').join(urls);

					Post newPost = Post.createPostWithSubject(postKey,
							"Ready for reviews (" + reviews.size() + "/" + reviewsToSend.size() + ")", body, authorName,
							GERRIT_ICON, reviews.iterator().next().getUpdated());
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

	private String mergeableText(PatchSet review) {
		String mergeable = ":boom:";
		if (review.isMergeable()) {
			mergeable = ":star:";
		}
		return mergeable;
	}

	public GerritLogger groupReviews(boolean value) {
		this.groupReviews = value;
		return this;
	}

	public Collection<String> findPotentialPatchsetIDs(String content) {
		Set<String> patchsetRefs = Sets.newLinkedHashSet();

		/*
		 * search for URLs
		 * 
		 * https://git.eclipse.org/r/#/c/76832/1
		 * https://git.eclipse.org/r/#/c/76832
		 * https://git.eclipse.org/r/#/c/76832
		 * 
		 * https://git.eclipse.org/r/32235
		 * 
		 */
		Pattern patchsetURL = Pattern.compile(this.serverURL + "/(#/c/)?(\\d+)/*");
		if (patchsetURL.matcher(content).find()) {
			Matcher matcher = patchsetURL.matcher(content);
			while (matcher.find()) {
				String found = matcher.group(2);
				patchsetRefs.add(found);
			}
		}

		return patchsetRefs;
	}

	public List<PatchSet> findGerritPatchsets(String content) {
		List<PatchSet> patchsets = Lists.newArrayList();
		Collection<String> potentialIds = findPotentialPatchsetIDs(content);
		if (potentialIds.size() > 0) {
			OkHttpClient client = new OkHttpClient();

			String changeString = Joiner.on(" OR ")
					.join(Iterables.transform(potentialIds, new Function<String, String>() {

						public String apply(String changeID) {
							return "change:" + changeID;
						}
					}));
			String url = serverURL + "/changes/?q=" + changeString + "&o=DETAILED_ACCOUNTS&o=CURRENT_REVISION";
			Request request = new Request.Builder().url(url).get().build();
			Response response;
			try {
				response = client.newCall(request).execute();
				String returnedJson = response.body().string();
				response.body().close();
				PatchSet[] reviews = gson.fromJson(returnedJson, PatchSet[].class);
				for (PatchSet patchSet : reviews) {
					patchsets.add(patchSet);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return patchsets;
	}

	public Collection<StringBuffer> getTableReport(List<PatchSet> issues) {
		List<StringBuffer> result = Lists.newArrayList();

		for (List<PatchSet> requests : Lists.partition(issues, 20)) {
			StringBuffer table = new StringBuffer();
			table.append("| Status | Summary | reporter | last update| link | \n");
			table.append("|---------------------------------------------------|\n");
			for (PatchSet req : requests) {
				long delay = (new Date().getTime() - req.getUpdated().getTime()) / (1000 * 60);
				table.append("| " + req.getStatus() + "   | " + GitLogger.detectBugzillaLink(req.getSubject()) + " | "
						+ req.getOwner().getName() + " | " + Dates.prettyDelayFromMinutes(delay) + "| [link]("
						+ serverURL + "/#/c/" + req.get_number() + ")");
				table.append("\n");
			}

			result.add(table);
		}

		return result;
	}

}
