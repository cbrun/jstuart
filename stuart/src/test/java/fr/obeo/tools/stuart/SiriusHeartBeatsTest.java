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
import com.google.common.hash.Hashing;

import fr.obeo.tools.stuart.bugzilla.BugzillaLogger;
import fr.obeo.tools.stuart.eclipseforum.EclipseForumsLogger;
import fr.obeo.tools.stuart.gerrit.GerritLogger;
import fr.obeo.tools.stuart.git.GitLogger;
import fr.obeo.tools.stuart.jenkins.JenkinsLogger;
import fr.obeo.tools.stuart.mattermost.MattermostEmitter;

public class SiriusHeartBeatsTest {

	@Test
	public void sendEventsToSiriusPrivateChan() throws Exception {

		// String host = "92.51.162.68";
		String host = "mattermost-test.eclipse.org";
		String storage = System.getenv("WORKSPACE");
		if (storage == null) {
			storage = ".";
		}

		String channel = System.getenv("MATTERMOST_CHANNEL");
		if (channel != null) {
			MattermostEmitter emitter = new MattermostEmitter("https", host, channel);

			int nbDays = 3;
			
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -nbDays);
			Date daysAgo = cal.getTime();

			EmitterTrace traceFile = new EmitterTrace(
					new File(storage + "/" + host + "_" + Hashing.sha256().hashString(channel) + "_trace.json"));
			Map<String, Date> trace = traceFile.load();

			List<Post> posts = Lists.newArrayList();
			posts.addAll(new GitLogger(new File(storage + "/clones/")).getMergedCommits(daysAgo,
					"https://git.eclipse.org/r/sirius/org.eclipse.sirius",
					"https://git.eclipse.org/c/sirius/org.eclipse.sirius.git/commit/?id="));
			posts.addAll(new EclipseForumsLogger(262, daysAgo).forumLog());

			posts.addAll(new JenkinsLogger("https://hudson.eclipse.org/sirius/", daysAgo).getBuildResults());
			posts.addAll(new GerritLogger("https://git.eclipse.org/r").getPatchsets(nbDays,
					Sets.newHashSet("sirius/org.eclipse.sirius")));
			posts.addAll(
					new BugzillaLogger("https://bugs.eclipse.org/bugs", Sets.newHashSet("genie", "genie@eclipse.org"))
							.bugzillaLog(3, Sets.newHashSet("Sirius")));

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
			Assert.fail("Expecting the MATTERMOST_CHANNEL environment variable to be set");
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
