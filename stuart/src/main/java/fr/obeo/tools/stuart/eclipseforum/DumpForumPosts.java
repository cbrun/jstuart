package fr.obeo.tools.stuart.eclipseforum;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import fr.obeo.tools.stuart.Dates;
import fr.obeo.tools.stuart.Post;

public class DumpForumPosts {

	private static Gson gson = new GsonBuilder().setDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz").setPrettyPrinting()
			.create();

	public static List<Post> updateOnProject(int forumID, String prjName) {
		return updateOnProject(forumID, prjName, Lists.newArrayList());
	}

	public static List<Post> updateOnProject(int forumID, String prjName, List<Post> newPosts2) {
		List<Post> alreadyThere = getPosts(prjName);
		Date mostRecent = null;
		Map<String, Post> postByKey = Maps.newLinkedHashMap();
		for (Post post : alreadyThere) {
			if (mostRecent == null) {
				mostRecent = post.getCreatedAt();
			} else {
				if (!mostRecent.after(post.getCreatedAt())) {
					mostRecent = post.getCreatedAt();
				}
			}
			postByKey.put(post.getKey(), post);
		}

		if (mostRecent == null) {
			mostRecent = Dates.getDateXDaysAgo(3650);
		}
		System.out.println("[" + prjName + "] old posts:" + postByKey.size());
		Collection<Post> newPosts = new EclipseForumsLogger(forumID, mostRecent).forumLog();
		newPosts2.addAll(newPosts);
		System.out.println("[" + prjName + "] new posts:" + newPosts.size() + " before:" + postByKey.values().size());
		for (Post post : newPosts) {
			postByKey.put(post.getKey(), post);
		}
		List<Post> posts = Lists.newArrayList(postByKey.values());
		Collections.sort(posts, new Comparator<Post>() {
			public int compare(Post m1, Post m2) {
				return m1.getCreatedAt().compareTo(m2.getCreatedAt());
			}
		});
		savePosts(prjName, posts);
		return posts;

	}

	private static void dumpOneProject(int nbWeeks, int forumID, String prjName) {

		Date date = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int i = c.get(Calendar.DAY_OF_WEEK) - c.getFirstDayOfWeek();
		c.add(Calendar.DATE, -i - 7);
		Date startOfLastWeek = c.getTime();
		c.add(Calendar.DATE, 6);
		Date endOfLastWeek = c.getTime();

		c.add(Calendar.WEEK_OF_YEAR, -nbWeeks);
		c.add(Calendar.DATE, -6);

		Date daysAgo = c.getTime();

		SimpleDateFormat f = new SimpleDateFormat("YYYY/MM/dd");
		System.out.println("stopping at  " + f.format(endOfLastWeek));

		System.err.println(f.format(daysAgo));

		Collection<Post> posts = new EclipseForumsLogger(forumID, daysAgo).forumLog();

		savePosts(prjName, posts);
	}

	public static void savePosts(String prjName, Collection<Post> posts) {
		File dataFile = getPostsDataFile(prjName);

		try (FileOutputStream out = new FileOutputStream(dataFile)) {
			Files.createParentDirs(dataFile);

			OutputStreamWriter writer = new OutputStreamWriter(out, "UTF-8");
			gson.toJson(posts, writer);
			writer.flush();
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static File getPostsDataFile(String prjName) {
		String outFolder = System.getProperty("user.dir") + "/results/";
		File dataFile = new File(outFolder + prjName + ".json");
		return dataFile;
	}

	public static Collection<Post> getSentimentsPosts(String prjName) {
		return getPosts(prjName + "-sentiments");
	}

	public static List<Post> getPosts(String prjName) {
		File dataFile = getPostsDataFile(prjName);
		try {
			return gson.fromJson(new InputStreamReader(new FileInputStream(dataFile), "UTF-8"),
					new TypeToken<List<Post>>() {
					}.getType());
		} catch (JsonSyntaxException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Lists.newArrayList();
	}

}
