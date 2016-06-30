package fr.obeo.tools.stuart;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

import fr.obeo.tools.stuart.UserRequests.PostWithAnswer;

public class UserRequests {

	public static class PostWithAnswer implements UserRequest {

		private Post post;

		private Optional<Post> answer = Optional.empty();

		public Optional<Post> getAnswer() {
			return answer;
		}

		public void setAnswer(Post answer) {
			this.answer = Optional.of(answer);
		}

		public PostWithAnswer(Post post) {
			super();
			this.post = post;
		}

		@Override
		public long getNbDaysSinceLastAnswer() {
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
			long diff = now.getTime() - post.getCreatedAt().getTime();
			long nbdays = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
			return nbdays;
		}

		@Override
		public String getSummary() {
			if (post.getSubject() != null && post.getSubject().length() > 0) {
				return post.getSubject();
			} else {
				return Splitter.on("\n").split(post.getMarkdownBody()).iterator().next();
			}
		}

		@Override
		public String getLastAuthorName() {
			if (answer.isPresent()) {
				answer.get().getAuthor();
			}
			return post.getAuthor();
		}

		@Override
		public String getReporterName() {
			return post.getAuthor();
		}

		@Override
		public String getUrl() {
			return post.getKey();
		}

		@Override
		public long getNbMinutesSinceLastAnswer() {
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
			long diff = now.getTime() - post.getCreatedAt().getTime();
			return diff / (1000 * 60);
		}

	}

	public static void sortByNbDaySinceAsked(List<UserRequest> forumIssues) {
		Collections.sort(forumIssues, new Comparator<UserRequest>() {
			public int compare(UserRequest m1, UserRequest m2) {
				return Long.valueOf(m1.getNbDaysSinceLastAnswer())
						.compareTo(Long.valueOf(m2.getNbDaysSinceLastAnswer()));
			}

		});
	}

	public static void sortByNbThreads(List<UserRequest> forumIssues, Multimap<String, UserRequest> requestsByAuthor) {
		Collections.sort(forumIssues, new Comparator<UserRequest>() {
			public int compare(UserRequest m1, UserRequest m2) {

				return Integer.valueOf(requestsByAuthor.get(m1.getLastAuthorName()).size())
						.compareTo(Integer.valueOf(requestsByAuthor.get(m2.getLastAuthorName()).size()));

			}
		});
	}

	public static String prettyDelayFromMinutes(long minutes) {
		if (minutes < 60) {
			return minutes + " min";
		} else if (minutes < 60 * 24) {
			return minutes / 60 + " hrs";
		} else if (minutes < 60 * 24 * 7) {
			return minutes / 1440 + " days";
		} else if (minutes < 60 * 24 * 31) {
			return minutes / (60 * 24 * 7) + " wks";
		} else if (minutes < 60 * 24 * 31 * 12) {
			return minutes / (60 * 24 * 31) + " months";
		} else {
			return minutes / (60 * 24 * 31 * 12) + " years";
		}
	}

	private static String[] congratsGifs = { "https://media.giphy.com/media/9g8PH1MbwTy4o/giphy.gif",
			"https://media.giphy.com/media/Mp4hQy51LjY6A/giphy.gif",
			"https://media.giphy.com/media/xTiTnAbu69LQDFTOEg/giphy.gif",
			"https://media.giphy.com/media/7rj2ZgttvgomY/giphy.gif",
			"https://media.giphy.com/media/XB6pGqvOfJqY8/giphy.gif" };

	public static Collection<StringBuffer> getTableReport(Collection<UserRequest> complete, int limitNbDays,
			Multimap<String, UserRequest> requestsByAuthor) {
		List<StringBuffer> result = Lists.newArrayList();
		ArrayList<UserRequest> tableContent = Lists
				.newArrayList(Iterables.filter(complete, new Predicate<UserRequest>() {

					@Override
					public boolean apply(UserRequest input) {
						return input.getNbDaysSinceLastAnswer() <= limitNbDays;
					}
				}));
		int nbTooOld = complete.size() - tableContent.size();
		if (tableContent.size() > 0) {
			for (List<UserRequest> requests : Lists.partition(tableContent, 20)) {
				StringBuffer table = new StringBuffer();
				table.append("| Summary | reporter | last author| delay so far | response/thread |\n");
				table.append("|---------------------------------------------------|\n");
				for (UserRequest req : requests) {
					Collection<UserRequest> allRequestsFromSameAuthor = requestsByAuthor.get(req.getLastAuthorName());
					int totalNbOfThread = allRequestsFromSameAuthor.size();
					int withResponse = 0;
					for (PostWithAnswer userRequest : Iterables.filter(allRequestsFromSameAuthor,
							PostWithAnswer.class)) {
						if (userRequest.getAnswer().isPresent()) {
							withResponse++;
						}
					}
					table.append("| [" + req.getSummary() + "](" + req.getUrl() + ")" + " | " + req.getReporterName()
							+ " | " + req.getLastAuthorName() + " |  "
							+ prettyDelayFromMinutes(req.getNbMinutesSinceLastAnswer()) + " | " + withResponse + "/"
							+ totalNbOfThread + "  |");
					table.append("\n");
				}

				result.add(table);
			}
		} else {
			StringBuffer table = new StringBuffer();
			table.append(
					"Congrats!  **No one** has been waiting for more than " + limitNbDays + " days for an answer !\n");
			table.append("![](" + congratsGifURL() + ")\n");
			result.add(table);
		}
		if (nbTooOld > 0) {
			StringBuffer table = new StringBuffer();
			if (result.size() > 0) {
				table = Iterables.getLast(result);
			}
			table.append("*There are " + nbTooOld + " additional threads waiting for an answer for more than "
					+ limitNbDays + " days.*");
		}
		return result;
	}

	public static Collection<StringBuffer> getTableReport(Collection<UserRequest> complete, int limitNbDays) {
		List<StringBuffer> result = Lists.newArrayList();
		ArrayList<UserRequest> tableContent = Lists
				.newArrayList(Iterables.filter(complete, new Predicate<UserRequest>() {

					@Override
					public boolean apply(UserRequest input) {
						return input.getNbDaysSinceLastAnswer() <= limitNbDays;
					}
				}));
		int nbTooOld = complete.size() - tableContent.size();
		if (tableContent.size() > 0) {
			for (List<UserRequest> requests : Lists.partition(tableContent, 20)) {
				StringBuffer table = new StringBuffer();
				table.append("| Summary | reporter | last author| days ago | \n");
				table.append("|---------------------------------------------------|\n");
				for (UserRequest req : requests) {
					table.append("| [" + req.getSummary() + "](" + req.getUrl() + ")" + " | " + req.getReporterName()
							+ " | " + req.getLastAuthorName() + " |  " + req.getNbDaysSinceLastAnswer() + " days |");
					table.append("\n");
				}

				result.add(table);
			}
		} else {
			StringBuffer table = new StringBuffer();
			table.append(
					"Congrats!  **No one** has been waiting for more than " + limitNbDays + " days for an answer !\n");
			table.append("![](" + congratsGifURL() + ")\n");
			result.add(table);
		}
		if (nbTooOld > 0) {
			StringBuffer table = new StringBuffer();
			if (result.size() > 0) {
				table = Iterables.getLast(result);
			}
			table.append("*There are " + nbTooOld + " additional bugzillas waiting for an answer for more than "
					+ limitNbDays + " days.*");
		}
		return result;
	}

	public static String congratsGifURL() {
		int index = new Random().nextInt(congratsGifs.length);
		return congratsGifs[index];
	}

	public static PostWithAnswer createPostWithAnswer(Post firstPost) {
		return new PostWithAnswer(firstPost);
	}

}
