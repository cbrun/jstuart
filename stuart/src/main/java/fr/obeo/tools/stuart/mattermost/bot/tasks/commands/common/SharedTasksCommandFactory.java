package fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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

public class SharedTasksCommandFactory {

	/**
	 * For now we do IRC-style commands.
	 */
	private final static String COMMAND_STARTER = "!";

	private static final String VERB_STATUS = "Status";
	private static final String VERB_CREATE = "Create";
	private static final String VERB_ADDME = "AddMe";
	private static final String VERB_REMOVEME = "RemoveMe";
	private static final String VERB_TODO = "Todo";
	private static final String VERB_REROLL = "Reroll";
	private static final String VERB_DONE = "Done";
	private static final List<String> ALL_VERBS = Stream
			.of(VERB_STATUS, VERB_CREATE, VERB_ADDME, VERB_REMOVEME, VERB_TODO, VERB_REROLL, VERB_DONE)
			.collect(Collectors.toList());

	private static final String KEYWORD_TASKS = "tasks";

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

		List<String> trimmedArguments = Arrays.asList(commandText.trim().split(" ")).stream().map(String::trim)
				.collect(Collectors.toList());
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
		if (!(trimmedCoreArguments.size() >= 2)) {
			return new ErrorCommand(commandText, "There should be at least a task name and a verb.");
		} else {
			String taskName = trimmedCoreArguments.get(0);
			List<String> issues = getIssuesWithTaskName(taskName);
			if (!issues.isEmpty()) {
				return new ErrorCommand(commandText, "Invalid task name: \"" + taskName + "\": "
						+ issues.stream().collect(Collectors.joining("; ", "", ".")));
			} else {
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
					final String doneUserId;
					if (trimmedCoreArguments.size() >= 3) {
						String userSpecification = trimmedCoreArguments.get(2);
						// TODO interpret text to find user ID from the channel
						doneUserId = userSpecification;
					} else {
						doneUserId = userId;
					}
					return new DoneCommand(commandText, taskName, channelId, doneUserId);
				} else {
					return new ErrorCommand(commandText, "Unknown action: \"" + verbLiteral + "\".");
				}
			}
		}
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
}
