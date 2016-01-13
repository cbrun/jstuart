package fr.obeo.tools.stuart.jenkins;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.MultiMap;

import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
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
import fr.obeo.tools.stuart.jenkins.model.testResults.ChildReport;
import fr.obeo.tools.stuart.jenkins.model.testResults.TestCase;
import fr.obeo.tools.stuart.jenkins.model.testResults.TestReport;
import fr.obeo.tools.stuart.jenkins.model.testResults.TestSuite;

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
		return getBuildResults(Sets.<String> newLinkedHashSet());
	}

	public Collection<Post> getBuildResults(Set<String> alreadySent) {
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
						String postKey = lastBuild.getUrl();
						if (!alreadySent.contains(postKey)) {
							boolean manualTrigger = false;
							int totalCount = 0;
							boolean hasRecentRegressions = false;
							int failCount = 0;
							int skipCount = 0;
							String testReportURLName = null;

							String testsResults = "";

							for (BuildAction action : lastBuild.getActions()) {
								totalCount += action.getTotalCount();
								failCount += action.getFailCount();
								skipCount += action.getSkipCount();
								if (totalCount > 0) {
									testReportURLName = action.getUrlName();
									String rootReportURL = j.getLastBuild().getUrl() + testReportURLName;
									Request requestXMLTestReport = new Request.Builder()
											.url(rootReportURL + "/api/json?pretty=true").get().build();
									TestReport rootReport = gson.fromJson(
											client.newCall(requestXMLTestReport).execute().body().charStream(),
											TestReport.class);
									System.out.println("JenkinsLogger.getBuildResults()");

									Map<String, TestReport> reports = allNonEmptyReports(rootReport, rootReportURL);
									StringBuffer reportString = new StringBuffer();
									reportString.append(
											"| Configuration        | Duration  | All  | Failed | Skipped | Age |\n");
									reportString.append(
											"| -------------------- |:---------:| ----:|-------:|--------:|-----|\n");
									for (Map.Entry<String, TestReport> r : reports.entrySet()) {
										if (r.getValue().getFailCount() > 0) {
											reportString.append("| " + r.getKey() + "         | "
													+ Math.round(r.getValue().getDuration() / 60) + " min | "
													+ r.getValue().getTotalCount() + " |" + r.getValue().getFailCount()
													+ " | " + r.getValue().getSkipCount() + "|   |\n");
											for (TestSuite suite : r.getValue().getSuites()) {
												List<TestCase> casesToDisplay = Lists.newArrayList(
														Iterables.filter(suite.getCases(), new Predicate<TestCase>() {

															@Override
															public boolean apply(TestCase input) {
																return "FAILED".equals(input.getStatus())
																		&& input.getAge() < 3;
															}
														}));
												if (casesToDisplay.size() > 0) {
													hasRecentRegressions = true;
													for (TestCase tCase : casesToDisplay) {
														String className = tCase.getClassName();
														if (className.lastIndexOf(".") != -1) {
															className = className
																	.substring(className.lastIndexOf(".") + 1);
														}
														if (tCase.getAge() <= 2) {
															reportString.append("|**" + className + "."
																	+ tCase.getName() + "**|"
																	+ Math.round(tCase.getDuration()) + " sec | " + " "
																	+ "|" + " " + "|" + " " + "|**" + tCase.getAge()
																	+ "**|\n");
														} else {
															reportString.append("|" + className + "."
																	+ tCase.getName() + "| "
																	+ Math.round(tCase.getDuration()) + " sec|" + " "
																	+ "|" + " " + "|" + " " + "|" + tCase.getAge()
																	+ "|\n");
														}

													}

												}

											}
										}
									}
									if (reports.entrySet().size() > 0) {
										testsResults = reportString.toString();
									}
								}
								for (BuildCause cause : action.getCauses()) {
									if (cause.getShortDescription() != null) {
										manualTrigger = cause.getShortDescription().contains("user")
												|| cause.getShortDescription().contains("utilisateur");
									}

								}
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
									comments.add(
											Iterables.getFirst(Splitter.on('\n').split(i.getComment()), "no commit"));
								}
							}
							String authorTxt = "...";
							if (authors.size() <= 2 && authors.size() > 0) {
								authorTxt = Joiner.on(',').join(authors);
							}
							if (manualTrigger || hasRecentRegressions) {
								String body = "";
								if (hasRecentRegressions) {
									body += "\n\n" + testsResults + "\n";
								} else {
									body = Joiner.on('\n').join(comments);
								}
								Post newPost = Post.createPostWithSubject(postKey,
										lastBuild.getFullDisplayName() + " is " + lastBuild.getResult(), body,
										authorTxt, JENKINS_ICON, new Date(lastBuild.getTimestamp()));
								newPost.mightBeTruncated(!hasRecentRegressions);
								newPost.setQuote(false);
								newPost.addURLs(postKey);
								posts.add(newPost);
							}
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

	public static Map<String, TestReport> allNonEmptyReports(TestReport rep, String rootURL) {
		Map<String, TestReport> result = Maps.newLinkedHashMap();
		if (rep.getChildReports().size() > 0) {
			for (ChildReport childReport : rep.getChildReports()) {
				TestReport childTestResults = childReport.getResult();
				if (childTestResults.getSkipCount() > 0 || childTestResults.getPassCount() > 0) {
					String name = childReport.getChild().getUrl()
							.substring(childReport.getChild().getUrl().indexOf("./") + 2);
					String url = "[" + name + "](" + childReport.getChild().getUrl() + ")";
					result.put(url, childTestResults);
				}
			}
		} else if (rep.getTotalCount() > 0) {
			result.put(rootURL, rep);
		}
		return result;
	}

}
