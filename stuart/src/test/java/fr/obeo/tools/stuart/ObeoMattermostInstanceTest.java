package fr.obeo.tools.stuart;

import java.io.File;
import java.net.URL;
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

import fr.obeo.tools.stuart.eclipseforum.PolarsysForumsLogger;
import fr.obeo.tools.stuart.gerrit.GerritLogger;
import fr.obeo.tools.stuart.git.GitLogger;
import fr.obeo.tools.stuart.jenkins.JenkinsLogger;
import fr.obeo.tools.stuart.mattermost.MattermostEmitter;
import fr.obeo.tools.stuart.rss.RssLogger;

public class ObeoMattermostInstanceTest {

	private static final String SO_ICON = "https://veithen.github.io/images/icon-stackoverflow.svg";

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
			MattermostEmitter emitter = new MattermostEmitter("https", host, channel);

			int nbDays = 10;

			Date daysAgo = getDateXDaysAgo(nbDays);

			EmitterTrace traceFile = new EmitterTrace(new File(storage + "/" + host + "_eefproperties_trace.json"));
			Map<String, Date> trace = traceFile.load();

			List<Post> posts = Lists.newArrayList();
			posts.addAll(new GitLogger(new File(storage + "/clones/")).getMergedCommits(daysAgo,
					"https://git.eclipse.org/r/eef/org.eclipse.eef",
					"https://git.eclipse.org/c/eef/org.eclipse.eef.git/commit/?id="));
			posts.addAll(new GitLogger(new File(storage + "/clones/")).getMergedCommits(daysAgo,
					"https://github.com/sbegaudeau/eef.git", "https://github.com/sbegaudeau/eef/commit/"));

			posts.addAll(new JenkinsLogger("https://hudson.eclipse.org/eef/", daysAgo).getBuildResults(trace.keySet()));
			posts.addAll(new GerritLogger("https://git.eclipse.org/r")
					.getPatchsets(Sets.newHashSet("eef/org.eclipse.eef"), nbDays));

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

	@Test
	public void sendEventsToUMLDesignerChan() throws Exception {
		String storage = System.getenv("WORKSPACE");
		if (storage == null) {
			storage = ".";
		}

		String channel = System.getenv("UML_CHANNEL");
		if (channel != null) {
			MattermostEmitter emitter = new MattermostEmitter("https", host, channel);

			int nbDays = 15;

			Date daysAgo = getDateXDaysAgo(nbDays);

			EmitterTrace traceFile = new EmitterTrace(new File(storage + "/" + host + "_uml_trace.json"));
			Map<String, Date> trace = traceFile.load();

			List<Post> posts = Lists.newArrayList();
			try {
				posts.addAll(new RssLogger(new URL("https://stackoverflow.com/feeds/tag/uml-designer"), daysAgo)
						.setIcon(SO_ICON).get());
			} catch (RuntimeException e) {
				/*
				 * we do expect such exception because there is not a single
				 * post with uml-designer as a tag yet (08-2016)
				 */
			}

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
			Assert.fail("Expecting the UML_CHANNEL environment variable to be set");
		}
	}
	
	@Test
	public void sendEventsToM2DocChan() throws Exception {

		String storage = System.getenv("WORKSPACE");
		if (storage == null) {
			storage = ".";
		}

		String channel = System.getenv("M2DOC_CHANNEL");
		if (channel != null) {
			MattermostEmitter emitter = new MattermostEmitter("https", host, channel);

			int nbDays = 10;

			Date daysAgo = getDateXDaysAgo(nbDays);

			EmitterTrace traceFile = new EmitterTrace(new File(storage + "/" + host + "_m2docproperties_trace.json"));
			Map<String, Date> trace = traceFile.load();

			List<Post> posts = Lists.newArrayList();
			posts.addAll(new GitLogger(new File(storage + "/clones/")).getMergedCommits(daysAgo,
					"https://github.com/ObeoNetwork/M2Doc.git", "https://github.com/ObeoNetwork/M2Doc/commit/"));
			posts.addAll(new RssLogger(new URL("https://stackoverflow.com/feeds/tag/m2doc"), daysAgo).setIcon(SO_ICON)
					.get());
			//posts.addAll(new PolarsysForumsLogger("m2doc", daysAgo).forumLog());

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
			Assert.fail("Expecting the M2DOC_CHANNEL environment variable to be set");
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
