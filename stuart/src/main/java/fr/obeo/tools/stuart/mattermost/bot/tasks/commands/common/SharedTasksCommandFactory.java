package fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.obeo.tools.stuart.mattermost.bot.MPost;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.AddMeCommand;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.CreateTaskCommand;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.DoneCommand;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.ErrorCommand;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.RemoveMeCommand;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.RerollCommand;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.StatusCommand;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.TodoCommand;

/**
 * Factory that can parse a Mattermost post to create the corresponding
 * {@link SharedTasksCommand}.
 * 
 * @author flatombe
 *
 */
public class SharedTasksCommandFactory {

	/**
	 * For now we do IRC-style commands.
	 */
	public final static String COMMAND_STARTER = "!";

	public final static String COMMAND_SEPARATOR = " ";

	public static final String VERB_STATUS = "Status";
	public static final String VERB_CREATE = "Create";
	public static final String VERB_ADDME = "AddMe";
	public static final String VERB_REMOVEME = "RemoveMe";
	public static final String VERB_TODO = "Todo";
	public static final String VERB_REROLL = "Reroll";
	public static final String VERB_DONE = "Done";
	public static final List<String> ALL_VERBS = Stream
			.of(VERB_STATUS, VERB_CREATE, VERB_ADDME, VERB_REMOVEME, VERB_TODO, VERB_REROLL, VERB_DONE)
			.collect(Collectors.toList());

	private static final String KEYWORD_TASKS = "tasks";

	/**
	 * Attemps to parse a {@link MPost Mattermost post} into a
	 * {@link SharedTasksCommand}.
	 * 
	 * @param mattermostPost the (non-{@code null}) {@link MPost} to parse.
	 * @return the corresponding {@link SharedTasksCommand}, or {@code null} if it
	 *         could not be parsed as one.
	 */
	public static SharedTasksCommand tryToParsePostIntoCommand(MPost mattermostPost) {
		String userMessage = mattermostPost.getMessage();
		if (userMessage.startsWith(COMMAND_STARTER)) {
			return parseFrom(userMessage.substring(COMMAND_STARTER.length()), mattermostPost.getChannelId(),
					mattermostPost.getUserId());
		} else {
			return null;
		}
	}

	/**
	 * Parses the text entered by a user that corresponds to a command into a
	 * {@link SharedTasksCommand}.
	 * 
	 * @param commandText the (non-{@code null}) input text, stripped from the
	 *                    command started symbol(s).
	 * @param channelId   the (non-{@code null}) ID of the channel of the
	 *                    originating post.
	 * @param userId      the (non-{@code null}) ID of the user of the originating
	 *                    post.
	 * @return the corresponding {@link SharedTasksCommand}.
	 */
	private static SharedTasksCommand parseFrom(String commandText, String channelId, String userId) {
		Objects.requireNonNull(commandText);
		Objects.requireNonNull(channelId);
		Objects.requireNonNull(userId);

		List<String> trimmedArguments = Arrays
				.asList(commandText.trim().split(SharedTasksCommandFactory.COMMAND_SEPARATOR)).stream()
				.map(String::trim).collect(Collectors.toList());
		if (trimmedArguments.isEmpty()) {
			return new ErrorCommand(commandText, "Empty text cannot be parsed as a command");
		} else {
			if (trimmedArguments.get(0).equalsIgnoreCase(KEYWORD_TASKS)) {
				return parseFrom(commandText, trimmedArguments.subList(1, trimmedArguments.size()), channelId, userId);
			} else {
				return parseFrom(commandText, trimmedArguments, channelId, userId);
			}
		}
	}

	private static SharedTasksCommand parseFrom(String commandText, List<String> trimmedCoreArguments, String channelId,
			String userId) {

		Optional<String> errorMessage = getIssuesWithCommand(trimmedCoreArguments);
		if (!errorMessage.isPresent()) {
			String taskName = trimmedCoreArguments.get(0);
			String verbLiteral = trimmedCoreArguments.get(1);
			if (verbLiteral.equalsIgnoreCase(VERB_STATUS)) {
				return new StatusCommand(commandText, taskName, channelId);
			} else if (verbLiteral.equalsIgnoreCase(VERB_CREATE)) {
				return new CreateTaskCommand(commandText, taskName, channelId);
			} else if (verbLiteral.equalsIgnoreCase(VERB_ADDME)) {
				return new AddMeCommand(commandText, taskName, channelId, userId);
			} else if (verbLiteral.equalsIgnoreCase(VERB_REMOVEME)) {
				return new RemoveMeCommand(commandText, taskName, channelId, userId);
			} else if (verbLiteral.equalsIgnoreCase(VERB_TODO)) {
				return new TodoCommand(commandText, taskName, channelId);
			} else if (verbLiteral.equalsIgnoreCase(VERB_REROLL)) {
				return new RerollCommand(commandText, taskName, channelId);
			} else if (verbLiteral.equalsIgnoreCase(VERB_DONE)) {
				// There may optionally be a user designated after the verb.
				final String doneUserId;
				if (trimmedCoreArguments.size() >= 3) {
					String userSpecification = trimmedCoreArguments.get(2);
					// TODO interpret text to find user ID from the channel
					doneUserId = userSpecification;
				} else {
					doneUserId = userId;
				}
				return new DoneCommand(commandText, taskName, channelId, doneUserId);
			}
		}

		return new ErrorCommand(commandText, errorMessage.get());
	}

	private static Optional<String> getIssuesWithCommand(List<String> trimmedCoreArguments) {
		List<String> issues = new ArrayList<>();

		if (!(trimmedCoreArguments.size() >= 2)) {
			issues.add("There should be at least a task name and a verb.");
		} else {
			String verbName = trimmedCoreArguments.get(1);
			issues.addAll(getIssuesWithVerbName(verbName));
		}
		if (trimmedCoreArguments.size() >= 1) {
			String taskName = trimmedCoreArguments.get(0);
			issues.addAll(getIssuesWithTaskName(taskName));
		}

		String errorMessage = issues.stream().collect(Collectors.joining("\n"));
		return issues.isEmpty() ? Optional.empty() : Optional.of(errorMessage);
	}

	private static List<String> getIssuesWithTaskName(String taskName) {
		// TODO: check that the task name is valid, in particular with regards to the
		// naming we can use in spreadsheet sheet names.
		List<String> issues = new ArrayList<>();
		if (taskName.equalsIgnoreCase(KEYWORD_TASKS)) {
			issues.add("Task name may not be \"" + KEYWORD_TASKS + "\".");
		}

		return issues;
	}

	private static List<String> getIssuesWithVerbName(String taskName) {
		List<String> issues = new ArrayList<>();
		boolean correctVerb = ALL_VERBS.stream().filter(s -> s.toLowerCase().equals(taskName.toLowerCase())).findFirst()
				.isPresent();
		if (!correctVerb) {
			issues.add("\"" + taskName + "\" is an not a valid verb. Please choose among " + ALL_VERBS);
		}

		return issues;
	}
}
