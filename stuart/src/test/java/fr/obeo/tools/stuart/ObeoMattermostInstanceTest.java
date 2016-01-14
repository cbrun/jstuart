package fr.obeo.tools.stuart;

import java.io.File;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import fr.obeo.tools.stuart.gerrit.GerritLogger;
import fr.obeo.tools.stuart.git.GitLogger;
import fr.obeo.tools.stuart.jenkins.JenkinsLogger;
import fr.obeo.tools.stuart.mattermost.MattermostEmitter;

public class ObeoMattermostInstanceTest {

	private String host = "mattermost.obeo.fr";

	

	public static Date getDateXDaysAgo(int nbDays) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -nbDays);
		Date daysAgo = cal.getTime();
		return daysAgo;
	}

	@Test
	public void sendEventsToEEFPrivateChan() throws Exception {

		String storage = System.getenv("WORKSPACE");
		if (storage == null) {
			storage = ".";
		}

		String channel = System.getenv("EEF_CHANNEL");
		if (channel != null) {
			MattermostEmitter emitter = new MattermostEmitter("http", host, channel);

			int nbDays = 10;

			Date daysAgo = getDateXDaysAgo(nbDays);

			EmitterTrace traceFile = new EmitterTrace(
					new File(storage + "/" + host + "_eefproperties_trace.json"));
			Map<String, Date> trace = traceFile.load();

			List<Post> posts = Lists.newArrayList();
			posts.addAll(new GitLogger(new File(storage + "/clones/")).getMergedCommits(daysAgo,
					"https://git.eclipse.org/r/eef/org.eclipse.eef",
					"https://git.eclipse.org/c/eef/org.eclipse.eef.git/commit/?id="));
			posts.addAll(new GitLogger(new File(storage + "/clones/")).getMergedCommits(daysAgo,
					"https://github.com/sbegaudeau/eef.git",
					"https://github.com/sbegaudeau/eef/commit/"));

			posts.addAll(
					new JenkinsLogger("https://hudson.eclipse.org/eef/", daysAgo).getBuildResults(trace.keySet()));
			posts.addAll(new GerritLogger("https://git.eclipse.org/r", nbDays)
					.getPatchsets(Sets.newHashSet("eef/org.eclipse.eef")));

			Collections.sort(posts, new Comparator<Post>() {
				public int compare(Post m1, Post m2) {
					return m1.getCreatedAt().compareTo(m2.getCreatedAt());
				}
			});

			for (Post post : posts) {
				send(emitter, trace, post);
			}
			traceFile.evictOldEvents(trace, 60);
			traceFile.save(trace);
		} else {
			Assert.fail("Expecting the EEF_CHANNEL environment variable to be set");
		}
	}

	private void send(MattermostEmitter emitter, Map<String, Date> trace, Post post) {
		if (!trace.containsKey(post.getKey())) {
			try {
				System.err.println("Sending :" + post.getKey());
				emitter.accept(MattermostPost.fromGenericPost(post));
				trace.put(post.getKey(), new Date());
				Thread.sleep(500);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
