package fr.obeo.tools.stuart;

import java.io.File;
import java.net.URL;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.hash.Hashing;

import fr.obeo.tools.stuart.bugzilla.BugzillaLogger;
import fr.obeo.tools.stuart.eclipseforum.EclipseForumsLogger;
import fr.obeo.tools.stuart.gerrit.GerritLogger;
import fr.obeo.tools.stuart.git.GitLogger;
import fr.obeo.tools.stuart.jenkins.JenkinsLogger;
import fr.obeo.tools.stuart.mattermost.MattermostEmitter;
import fr.obeo.tools.stuart.rss.RssLogger;

public class EclipseMattermostInstanceTest {

	private static final String SO_ICON = "https://veithen.github.io/images/icon-stackoverflow.svg";
	private String host = "mattermost.eclipse.org";

	@Test
	public void eclipseAnnounces() throws Exception {
		String storage = System.getenv("WORKSPACE");
		if (storage == null) {
			storage = ".";
		}

		String channel = System.getenv("NEWS_CHANNEL");
		if (channel != null) {
			MattermostEmitter emitter = new MattermostEmitter("https", host, channel);

			Date daysAgo = getDateXDaysAgo(15);

			EmitterTrace traceFile = new EmitterTrace(new File(
					storage + "/" + host + "_" + Hashing.sha256().hashString(channel, Charsets.UTF_8) + "_trace.json"));
			Map<String, Date> trace = traceFile.load();

			List<Post> posts = Lists.newArrayList();
			posts.addAll(
					new RssLogger(new URL("https://dev.eclipse.org/mhonarc/lists/eclipse.org-committers/maillist.rss"),
							daysAgo).get());
			posts.addAll(new RssLogger(new URL("https://newsroom.eclipse.org/news/announcements.xml"), daysAgo).get());

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
			Assert.fail("Expecting the NEWS_CHANNEL environment variable to be set");
		}
	}

	/** Sends events to the webtools channel */
	@Test
	public void sendEventsToWebtoolsChannels() throws Exception {
		String storage = System.getenv("WORKSPACE");
		if (storage == null) {
			storage = ".";
		}
		// channels
		String webtoolsChannel = System.getenv("WEBTOOLS-BOTS-CHANNEL");
		if (webtoolsChannel == null) {
			webtoolsChannel = "webtools-bots";
		}

		MattermostEmitter webToolsEmitter = new MattermostEmitter("https", host, webtoolsChannel);

		int daysAgo = 3;
		Date dateAgo = getDateXDaysAgo(daysAgo);
		EmitterTrace traceFile = new EmitterTrace(new File(storage + "/" + host + "_webtools" + "_trace.json"));
		Map<String, Date> trace = traceFile.load();

		List<Post> posts = Lists.newArrayList();
		// forum WTP and Webtools incubator
		posts.addAll(new EclipseForumsLogger().collectPosts(88, dateAgo));
		posts.addAll(new EclipseForumsLogger().collectPosts(114, dateAgo));
		posts.addAll(new EclipseForumsLogger().collectPosts(251, dateAgo));

		// log stackoverflow posts with given tags
		String[] soTags = new String[] {"webtools","eclipse-wtp","jsdt","eclipse-tomcat","eclipse-jsp","eclipse-jsdt","eclipse+tomcat*"};
		for (String tag: soTags) {
			Collection<Post> feedLogger = new RssLogger(new URL("https://stackoverflow.com/feeds/tag/" + tag), dateAgo).setIcon(SO_ICON).get();
			posts.addAll(
					feedLogger);
		}

		posts.addAll(new BugzillaLogger("https://bugs.eclipse.org/bugs", Sets.newHashSet("genie", "genie@eclipse.org"))
				.bugzillaLog(daysAgo, Sets.newHashSet("WebTools"), Collections.EMPTY_LIST, Collections.EMPTY_LIST, Collections.EMPTY_LIST));

		// log git commits
		posts.addAll(new GerritLogger("https://git.eclipse.org/r").groupReviews(false)
				.getPatchsets(Sets.newHashSet("jsdt/webtools.jsdt", "sourceediting/webtools.sourceediting", "webtools-common/webtools.common", "servertools/webtools.servertools", "webtools/webtools.releng", "jeetools/webtools.javaee"), 1));

		Collections.sort(posts, new Comparator<Post>() {
			public int compare(Post m1, Post m2) {
				return m1.getCreatedAt().compareTo(m2.getCreatedAt());
			}
		});

		for (Post post : posts) {
			send(webToolsEmitter, trace, post);
		}

		traceFile.evictOldEvents(trace, 60);
		traceFile.save(trace);
	}

	/** Sends events to the virgo channel */
	@Test
	public void sendEventsToVirgoChan() throws Exception {
		String storage = System.getenv("WORKSPACE");
		if (storage == null) {
			storage = ".";
		}

		// channels
		String virgoLogChannel = System.getenv("VIRGO_CHANNEL");

		String virgoMainChannel = System.getenv("VIRGO_MAIN_CHANNEL");

		MattermostEmitter virgoMainEmitter = new MattermostEmitter("https", host, virgoMainChannel);
		Date daysAgo = getDateXDaysAgo(3);
		EmitterTrace traceFile = new EmitterTrace(new File(storage + "/" + host + "_virgo" + "_trace.json"));
		Map<String, Date> trace = traceFile.load();

		for (Post post : new EclipseForumsLogger().collectPosts(159, daysAgo)) {
			send(virgoMainEmitter, trace, post);
		}

		List<Post> posts = Lists.newArrayList();
		MattermostEmitter virgoLogEmitter = new MattermostEmitter("https", host, virgoLogChannel);
		posts.addAll(
				new RssLogger(new URL("https://dev.eclipse.org/mhonarc/lists/virgo-dev/maillist.rss"), daysAgo).get());

		//posts.addAll(new JenkinsLogger("https://hudson.eclipse.org/virgo/", daysAgo).getBuildResults());

		posts.addAll(new BugzillaLogger("https://bugs.eclipse.org/bugs", Sets.newHashSet("genie", "genie@eclipse.org"))
				.bugzillaLog(3, Sets.newHashSet("Virgo")));

		Collections.sort(posts, new Comparator<Post>() {
			public int compare(Post m1, Post m2) {
				return m1.getCreatedAt().compareTo(m2.getCreatedAt());
			}
		});

		for (Post post : posts) {
			send(virgoLogEmitter, trace, post);
		}

		traceFile.evictOldEvents(trace, 60);
		traceFile.save(trace);
	}

	@Test
	public void sendEventsToAERIChannel() throws Exception {
		String storage = System.getenv("WORKSPACE");
		if (storage == null) {
			storage = ".";
		}

		// channels
		String aeriChannel = System.getenv("AERI-CHANNEL");

		MattermostEmitter aeriEmitter = new MattermostEmitter("https", host, aeriChannel);

		Date daysAgo = getDateXDaysAgo(3);
		EmitterTrace traceFile = new EmitterTrace(new File(storage + "/" + host + "_aeri" + "_trace.json"));
		Map<String, Date> trace = traceFile.load();

		List<Post> posts = Lists.newArrayList();

		posts.addAll(new JenkinsLogger("https://ci.eclipse.org/packaging/", daysAgo)
				.getBuildResults());

		for (Post post : new EclipseForumsLogger().collectPosts(69, daysAgo)) {

			if (post.getSubject() != null && post.getSubject().contains("[aeri]")) {
				posts.add(post);
			}

		}

		// log blog posts waiting for URL from Marcel
		// posts.addAll(
		// new RssLogger(new URL(""), daysAgo).get());

		posts.addAll(new BugzillaLogger("https://bugs.eclipse.org/bugs", Sets.newHashSet("genie", "genie@eclipse.org"))
				.bugzillaLog(3, Sets.newHashSet("EPP"), Sets.newHashSet("Error Reporting and Logging ")));

		posts.addAll(new GerritLogger("https://git.eclipse.org/r").groupReviews(false)
				.getPatchsets(Sets.newHashSet("epp/org.eclipse.epp.logging"), 1));

		Collections.sort(posts, new Comparator<Post>() {
			public int compare(Post m1, Post m2) {
				return m1.getCreatedAt().compareTo(m2.getCreatedAt());
			}
		});

		for (Post post : posts) {
			send(aeriEmitter, trace, post);
		}

		traceFile.evictOldEvents(trace, 60);
		traceFile.save(trace);
	}

	@Test
	public void sendEventsToPlatformChans() throws Exception {
		String storage = System.getenv("WORKSPACE");
		if (storage == null) {
			storage = ".";
		}

		String bug_Channel = System.getenv("PLATFORM_BUG_CHANNEL");
		String qa_Channel = System.getenv("PLATFORM_QA_CHANNEL");
		String patch_Channel = System.getenv("PLATFORM_PATCHES_CHANNEL");
		if (qa_Channel != null && bug_Channel != null && patch_Channel != null) {
			MattermostEmitter qaEmitter = new MattermostEmitter("https", host, qa_Channel);

			Date daysAgo = getDateXDaysAgo(3);

			EmitterTrace traceFile = new EmitterTrace(new File(storage + "/" + host + "_platform" + "_trace.json"));
			Map<String, Date> trace = traceFile.load();

			List<Post> posts = Lists.newArrayList();
			posts.addAll(new EclipseForumsLogger().collectPosts(11, daysAgo));
			posts.addAll(new EclipseForumsLogger().collectPosts(116, daysAgo));
			posts.addAll(new EclipseForumsLogger().collectPosts(106, daysAgo));
			posts.addAll(new EclipseForumsLogger().collectPosts(12, daysAgo));
			posts.addAll(new EclipseForumsLogger().collectPosts(100, daysAgo));
			posts.addAll(new EclipseForumsLogger().collectPosts(15, daysAgo));

			Collections.sort(posts, new Comparator<Post>() {
				public int compare(Post m1, Post m2) {
					return m1.getCreatedAt().compareTo(m2.getCreatedAt());
				}
			});

			posts.addAll(new RssLogger(new URL("https://stackoverflow.com/feeds/tag/eclipse-plugin"), daysAgo)
					.setIcon(SO_ICON).get());
			posts.addAll(new RssLogger(new URL("https://stackoverflow.com/feeds/tag/eclipse-rcp"), daysAgo)
					.setIcon(SO_ICON).get());
			posts.addAll(
					new RssLogger(new URL("https://stackoverflow.com/feeds/tag/swt"), daysAgo).setIcon(SO_ICON).get());
			posts.addAll(
					new RssLogger(new URL("https://stackoverflow.com/feeds/tag/jface"), daysAgo).setIcon(SO_ICON).get());
			posts.addAll(
					new RssLogger(new URL("https://stackoverflow.com/feeds/tag/e4"), daysAgo).setIcon(SO_ICON).get());

			for (Post post : posts) {
				send(qaEmitter, trace, post);
			}

			MattermostEmitter bugEmitter = new MattermostEmitter("https", host, bug_Channel);
			List<Post> bugzillas = Lists.newArrayList();
			bugzillas.addAll(
					new BugzillaLogger("https://bugs.eclipse.org/bugs", Sets.newHashSet("genie", "genie@eclipse.org"))
							.bugzillaLog(3, Sets.newHashSet("Platform")));

			for (Post post : bugzillas) {
				send(bugEmitter, trace, post);
			}

			List<Post> patches = Lists.newArrayList();
			patches.addAll(new GerritLogger("https://git.eclipse.org/r").groupReviews(false)
					.getPatchsets(Sets.newHashSet("platform/eclipse.platform", "platform/eclipse.platform.common",
							"platform/eclipse.platform.debug", "platform/eclipse.platform.images",
							"platform/eclipse.platform.news", "platform/eclipse.platform.resources",
							"platform/eclipse.platform.runtime", "platform/eclipse.platform.swt",
							"platform/eclipse.platform.team", "platform/eclipse.platform.text",
							"platform/eclipse.platform.ua", "platform/eclipse.platform.ui",
							"platform/eclipse.platform.tools"), 1));

			MattermostEmitter patchesEmitter = new MattermostEmitter("https", host, patch_Channel);
			for (Post post : patches) {
				send(patchesEmitter, trace, post);
			}

			traceFile.evictOldEvents(trace, 60);
			traceFile.save(trace);
		} else {
			Assert.fail(
					"Expecting the PLATFORM_QA_CHANNEL, PLATFORM_BUG_CHANNEL,PLATFORM_PATCHES_CHANNEL environment variable to be set");
		}
	}

	@Test
	public void sendEventsToCDTChans() throws Exception {
		String storage = System.getenv("WORKSPACE");
		if (storage == null) {
			storage = ".";
		}

		String qa_Channel = System.getenv("CDT_CHANNEL");
		if (qa_Channel != null) {
			MattermostEmitter qaEmitter = new MattermostEmitter("https", host, qa_Channel);

			Date daysAgo = getDateXDaysAgo(3);

			EmitterTrace traceFile = new EmitterTrace(new File(storage + "/" + host + "_cdtgeneral" + "_trace.json"));
			Map<String, Date> trace = traceFile.load();

			List<Post> posts = Lists.newArrayList();
			posts.addAll(new EclipseForumsLogger().collectPosts(80, daysAgo));
			posts.addAll(new RssLogger(new URL("https://stackoverflow.com/feeds/tag/eclipse-cdt"), daysAgo)
					.setIcon(SO_ICON).get());

			Collections.sort(posts, new Comparator<Post>() {
				public int compare(Post m1, Post m2) {
					return m1.getCreatedAt().compareTo(m2.getCreatedAt());
				}
			});

			for (Post post : posts) {
				send(qaEmitter, trace, post);
			}

			traceFile.evictOldEvents(trace, 60);
			traceFile.save(trace);
		} else {
			Assert.fail("Expecting the CDT_CHANNEL environment variable to be set");
		}
	}

	@Test
	public void sendEventsToPackageDrone() throws Exception {
		String storage = System.getenv("WORKSPACE");
		if (storage == null) {
			storage = ".";
		}
		String mainChannelID = System.getenv("PACKAGEDRONE_MAIN_CHANNEL");
		String log_Channel = System.getenv("PACKAGEDRONE_CHANNEL");
		if (log_Channel != null && mainChannelID != null) {
			MattermostEmitter emitter = new MattermostEmitter("https", host, log_Channel);

			Date daysAgo = getDateXDaysAgo(3);

			EmitterTrace traceFile = new EmitterTrace(new File(storage + "/" + host + "_mattermost" + "_trace.json"));
			Map<String, Date> trace = traceFile.load();

			List<Post> posts = Lists.newArrayList();
			posts.addAll(
					new BugzillaLogger("https://bugs.eclipse.org/bugs", Sets.newHashSet("genie", "genie@eclipse.org"))
							.bugzillaLog(3, Sets.newHashSet("Package-Drone")));
			posts.addAll(new GitLogger(new File(storage + "/clones/")).getMergedCommits(daysAgo,
					"https://github.com/eclipse/packagedrone.git", "https://github.com/eclipse/packagedrone/commit/"));
			posts.addAll(new EclipseForumsLogger().collectPosts(318, daysAgo));
			//posts.addAll(new JenkinsLogger("https://hudson.eclipse.org/package-drone/", daysAgo).getBuildResults());

			Collections.sort(posts, new Comparator<Post>() {
				public int compare(Post m1, Post m2) {
					return m1.getCreatedAt().compareTo(m2.getCreatedAt());
				}
			});

			for (Post post : posts) {
				send(emitter, trace, post);
			}
			/*
			 * For some reasons the packagedrone feed parsing is currently
			 * failing MattermostEmitter mainChannelEmitter = new
			 * MattermostEmitter("https", host, mainChannelID); for (Post post :
			 * new RssLogger(new URL("http://packagedrone.org/feed/"),
			 * daysAgo).get()) { send(mainChannelEmitter, trace, post); }
			 */

			traceFile.evictOldEvents(trace, 60);
			traceFile.save(trace);
		} else {
			Assert.fail("Expecting the PACKAGEDRONE_CHANNEL environment variable to be set");
		}
	}

	public static Date getDateXDaysAgo(int nbDays) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -nbDays);
		Date daysAgo = cal.getTime();
		return daysAgo;
	}

	@Test
	public void sendEventsToSiriusPrivateChan() throws Exception {

		String storage = System.getenv("WORKSPACE");
		if (storage == null) {
			storage = ".";
		}

		String channel = System.getenv("MATTERMOST_CHANNEL");
		if (channel != null) {
			MattermostEmitter emitter = new MattermostEmitter("https", host, channel);

			int nbDays = 3;

			EmitterTrace traceFile = new EmitterTrace(new File(
					storage + "/" + host + "_" + Hashing.sha256().hashString(channel, Charsets.UTF_8) + "_trace.json"));
			Map<String, Date> trace = traceFile.load();

			Date daysAgo = getDateXDaysAgo(nbDays);
			List<Post> posts = Lists.newArrayList();
			posts.addAll(new GitLogger(new File(storage + "/clones/")).getMergedCommits(daysAgo,
					"https://git.eclipse.org/r/sirius/org.eclipse.sirius",
					"https://git.eclipse.org/c/sirius/org.eclipse.sirius.git/commit/?id="));
			posts.addAll(new EclipseForumsLogger().collectPosts(262, daysAgo));

			posts.addAll(
					new JenkinsLogger("https://ci.eclipse.org/sirius/", daysAgo).getBuildResults(trace.keySet()));
			posts.addAll(new GerritLogger("https://git.eclipse.org/r")
					.getPatchsets(Sets.newHashSet("sirius/org.eclipse.sirius"), nbDays));
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

	@Test
	public void sendEventsToRoverChannel() throws Exception {
		String storage = System.getenv("WORKSPACE");
		if (storage == null) {
			storage = ".";
		}

		String rover_Channel = System.getenv("ROVER_CHANNEL");
		if (rover_Channel != null) {

			Date daysAgo = getDateXDaysAgo(80);
			MattermostEmitter acEmitter = new MattermostEmitter("https", host, rover_Channel);

			EmitterTrace traceFile = new EmitterTrace(
					new File(storage + "/" + host + "_rover_channel" + "_trace.json"));
			Map<String, Date> trace = traceFile.load();

			List<Post> posts = Lists.newArrayList();
			posts.addAll(new EclipseForumsLogger().setBaseURL("https://polarsys.org/forums/").collectPosts(20, daysAgo));

			Collections.sort(posts, new Comparator<Post>() {
				public int compare(Post m1, Post m2) {
					return m1.getCreatedAt().compareTo(m2.getCreatedAt());
				}
			});

			for (Post post : posts) {
				send(acEmitter, trace, post);
			}

			traceFile.evictOldEvents(trace, 60);
			traceFile.save(trace);
		} else {
			Assert.fail("Expecting the ROVER_CHANNEL environment variable to be set");
		}

	}

	private void send(MattermostEmitter emitter, Map<String, Date> trace, Post post) {
		if (!trace.containsKey(post.getKey())) {
			try {
				System.err.println("Sending : " + post.getKey());
				//emitter.accept(MattermostPost.fromGenericPost(post));
				trace.put(post.getKey(), new Date());
				Thread.sleep(500);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
