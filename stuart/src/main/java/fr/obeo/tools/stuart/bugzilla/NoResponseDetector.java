package fr.obeo.tools.stuart.bugzilla;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import b4j.core.Comment;
import b4j.core.DefaultIssue;
import b4j.core.DefaultSearchData;
import b4j.core.Issue;
import b4j.core.User;
import b4j.core.session.BugzillaHttpSession;
import fr.obeo.tools.stuart.UserRequest;

public class NoResponseDetector {

	private static final int LIMIT_DELAY_HOURS = 100;
	private Collection<String> teamMembers;
	private String baseURL;
	private Collection<String> authorsToIgnore;
	private Collection<String> products;
	private Collection<String> components;
	private Predicate<Comment> notGenie = new Predicate<Comment>() {

		@Override
		public boolean apply(Comment input) {
			return !authorsToIgnore.contains(input.getAuthor().getName());
		}
	};

	public NoResponseDetector(Collection<String> teamMembers, String baseURL, Collection<String> authorsToIgnore,
			Collection<String> products, Collection<String> components) {
		this.teamMembers = teamMembers;
		this.baseURL = baseURL;
		this.authorsToIgnore = authorsToIgnore;
		this.products = products;
		this.components = components;
	}

	public Collection<UserRequest> noResponseFromTeam() throws MalformedURLException {
		List<UserRequest> result = Lists.newArrayList();
		Multimap<String, Answer> delaysInAnswering = LinkedHashMultimap.create();
		Iterator<Issue> openedIssues = searchOpenedIssues();
		while (openedIssues.hasNext()) {
			IssueWithAnswer issue = new IssueWithAnswer(openedIssues.next());

			if (!teamMembers.contains(issue.getIssue().getReporter().getName())) {
				/*
				 * reported by a user
				 */
				Date lastQuestion = issue.getIssue().getCreationTimestamp();
				for (Comment comment : Iterables.filter(issue.getIssue().getComments(), notGenie)) {
					if (teamMembers.contains(comment.getAuthor().getName())) {
						Date answeredOn = comment.getCreationTimestamp();
						Answer answer = new Answer(lastQuestion, answeredOn, comment);
						if (answer.getNbHoursBeforeAnswer() < LIMIT_DELAY_HOURS) {
							delaysInAnswering.put(comment.getAuthor().getName(), answer);
						}
					} else {
						lastQuestion = comment.getCreationTimestamp();
					}

				}

				boolean reporterAutoAsign = issue.getIssue().getReporter().getName()
						.equals(issue.getIssue().getAssignee().getName());
				if (!reporterAutoAsign && issue.getIssue().getStatus().isOpen()
						&& !teamMembers.contains(issue.getLastAuthor().getName())) {
					result.add(issue);
				}
			}
		}
		List<Date> allDatesDates = Lists.newArrayList();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-ww");
		StringBuffer delay = new StringBuffer();
		for (String user : delaysInAnswering.keySet()) {
			Collection<Answer> answers = delaysInAnswering.get(user);
			for (Answer answer : answers) {
				allDatesDates.add(answer.getAnsweredOn());
			}
			delay.append(user + " : " + Joiner.on(",").join(answers) + "\n");
		}
		Collections.sort(allDatesDates);
		Set<String> allDates = Sets.newLinkedHashSet();
		for (Date date : allDatesDates) {
			allDates.add(sdf.format(date));
		}
		delay.append("\n");
		delay.append(" ;" + Joiner.on(" ; ").join(allDates) + "\n");
		for (String user : delaysInAnswering.keySet()) {
			Collection<Answer> answers = delaysInAnswering.get(user);
			StringBuffer buf = new StringBuffer();
			buf.append(user);
			buf.append(" ;");
			for (String date : allDates) {
				long highestDelay = -1;
				for (Answer answer : answers) {
					if (answer.getNbHoursBeforeAnswer() < LIMIT_DELAY_HOURS
							&& date.equals(sdf.format(answer.getAnsweredOn()))) {
						if (answer.getNbHoursBeforeAnswer() > highestDelay) {
							highestDelay = answer.getNbHoursBeforeAnswer();
						}

					}
				}
				if (highestDelay != -1) {
					buf.append(highestDelay);
					buf.append(" ");
				}
				buf.append(" ;");
			}
			delay.append(buf.toString());
		}
		delay.append("\n");
		return result;

	}

	private Iterator<Issue> searchOpenedIssues() throws MalformedURLException {
		BugzillaHttpSession session = new BugzillaHttpSession();
		session.setBaseUrl(new URL(this.baseURL));
		session.setBugzillaBugClass(DefaultIssue.class);
		Iterator<Issue> openedIssues = Iterators.emptyIterator();
		if (session.open()) {
			DefaultSearchData searchData = new DefaultSearchData();
			for (String productName : products) {
				searchData.add("product", productName);
			}
			for (String componentName : components) {
				searchData.add("component", componentName);
			}
			searchData.add("resolution", "---");

			openedIssues = session.searchBugs(searchData, null).iterator();

		}
		return openedIssues;
	}

	private Iterator<Issue> searchAllIssues() throws MalformedURLException {
		BugzillaHttpSession session = new BugzillaHttpSession();
		session.setBaseUrl(new URL(this.baseURL));
		session.setBugzillaBugClass(DefaultIssue.class);
		Iterator<Issue> allIssues = Iterators.emptyIterator();
		if (session.open()) {
			DefaultSearchData searchData = new DefaultSearchData();
			for (String productName : products) {
				searchData.add("product", productName);
			}
			for (String componentName : components) {
				searchData.add("component", componentName);
			}

			allIssues = session.searchBugs(searchData, null).iterator();

		}
		return allIssues;
	}

	public class IssueWithAnswer implements UserRequest {

		private Issue issue;

		public IssueWithAnswer(Issue next) {
			this.issue = next;
		}

		public Issue getIssue() {
			return this.issue;
		}

		public Date getLastUpdate() {
			Date lastUpdate = issue.getUpdateTimestamp();
			Comment lastComment = Iterables.getLast(Iterables.filter(issue.getComments(), notGenie));
			if (lastComment != null) {
				lastUpdate = lastComment.getCreationTimestamp();
			}
			return lastUpdate;
		}

		public User getLastAuthor() {
			User lastAuthor = issue.getReporter();
			Comment lastComment = Iterables.getLast(Iterables.filter(issue.getComments(), notGenie));
			if (lastComment != null) {

				lastAuthor = lastComment.getAuthor();
			}
			return lastAuthor;
		}

		public long getNbDaysSinceLastAnswer() {
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
			long diff = now.getTime() - getLastUpdate().getTime();
			long nbdays = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
			return nbdays;

		}

		@Override
		public String getSummary() {
			return getIssue().getSummary();
		}

		@Override
		public String getLastAuthorName() {
			return getLastAuthor().getName();
		}

		@Override
		public String getReporterName() {
			return issue.getReporter().getName();
		}

		@Override
		public String getUrl() {
			return issue.getUri();
		}

		@Override
		public long getNbMinutesSinceLastAnswer() {
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
			long diff = now.getTime() - getLastUpdate().getTime();
			return diff / (1000 * 60);
		}

	}

	public class Answer {

		private Comment answer;
		private Date questionOn;
		private Date answeredOn;

		public Answer(Date questionOn, Date answeredOn, Comment answer) {
			this.answer = answer;
			this.questionOn = questionOn;
			this.answeredOn = answeredOn;
		}

		public Comment getAnswer() {
			return answer;
		}

		public Date getQuestionOn() {
			return questionOn;
		}

		public Date getAnsweredOn() {
			return answeredOn;
		}

		@Override
		public String toString() {
			long nbHours = getNbHoursBeforeAnswer();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			return nbHours + " hours (" + sdf.format(questionOn) + " : " + this.answer.getId() + ")";
		}

		public long getNbHoursBeforeAnswer() {
			long nbHours = (answeredOn.getTime() - questionOn.getTime()) / (1000 * 3600);
			return nbHours;
		}

	}

}
