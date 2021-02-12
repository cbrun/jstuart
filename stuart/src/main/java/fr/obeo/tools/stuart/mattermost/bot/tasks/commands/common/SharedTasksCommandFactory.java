package fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import fr.obeo.tools.stuart.mattermost.MattermostSyntax;
import fr.obeo.tools.stuart.mattermost.bot.MMBot;
import fr.obeo.tools.stuart.mattermost.bot.MPost;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.AddMeCommand;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.CreateTaskCommand;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.DoneCommand;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.ErrorCommand;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.RemoveMeCommand;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.RerollCommand;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.StatusCommand;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.TodoCommand;
import fr.obeo.tools.stuart.mattermost.bot.user.MUser;

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

	private static final Function<String, String> VERB_STATUS_USAGE = taskName -> COMMAND_STARTER
			+ (taskName != null ? taskName : "<task name>") + COMMAND_SEPARATOR + VERB_STATUS;
	private static final Function<String, String> VERB_CREATE_USAGE = taskName -> COMMAND_STARTER
			+ (taskName != null ? taskName : "<task name>") + COMMAND_SEPARATOR + VERB_CREATE;
	private static final Function<String, String> VERB_ADDME_USAGE = taskName -> COMMAND_STARTER
			+ (taskName != null ? taskName : "<task name>") + COMMAND_SEPARATOR + VERB_ADDME;
	private static final Function<String, String> VERB_REMOVEME_USAGE = taskName -> COMMAND_STARTER
			+ (taskName != null ? taskName : "<task name>") + COMMAND_SEPARATOR + VERB_REMOVEME;
	private static final Function<String, String> VERB_TODO_USAGE = taskName -> COMMAND_STARTER
			+ (taskName != null ? taskName : "<task name>") + COMMAND_SEPARATOR + VERB_TODO;
	private static final Function<String, String> VERB_REROLL_USAGE = taskName -> COMMAND_STARTER
			+ (taskName != null ? taskName : "<task name>") + COMMAND_SEPARATOR + VERB_REROLL;
	private static final Function<String, String> VERB_DONE_USAGE = taskName -> COMMAND_STARTER
			+ (taskName != null ? taskName : "<task name>") + COMMAND_SEPARATOR + VERB_DONE + COMMAND_SEPARATOR
			+ "(<user name>)";

	public static final Map<String, Function<String, String>> ALL_VERBS_USAGE = createVerbsUsageMap();

	private static Map<String, Function<String, String>> createVerbsUsageMap() {
		Map<String, Function<String, String>> map = new LinkedHashMap<>();
		map.put(VERB_STATUS, VERB_STATUS_USAGE);
		map.put(VERB_CREATE, VERB_CREATE_USAGE);
		map.put(VERB_ADDME, VERB_ADDME_USAGE);
		map.put(VERB_REMOVEME, VERB_REMOVEME_USAGE);
		map.put(VERB_TODO, VERB_TODO_USAGE);
		map.put(VERB_REROLL, VERB_REROLL_USAGE);
		map.put(VERB_DONE, VERB_DONE_USAGE);
		return map;
	}

	private static final String KEYWORD_TASKS = "tasks";

	private final MMBot bot;

	public SharedTasksCommandFactory(MMBot bot) {
		this.bot = bot;
	}

	/**
	 * Attemps to parse a {@link MPost Mattermost post} into a
	 * {@link SharedTasksCommand}.
	 * 
	 * @param mattermostPost the (non-{@code null}) {@link MPost} to parse.
	 * @return the corresponding {@link SharedTasksCommand}, or {@code null} if it
	 *         could not be parsed as one.
	 */
	public SharedTasksCommand tryToParsePostIntoCommand(MPost mattermostPost) {
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
	private SharedTasksCommand parseFrom(String commandText, String channelId, String userId) {
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

	/**
	 * Parses the text entered by a user that corresponds to a command into a
	 * {@link SharedTasksCommand}.
	 * 
	 * @param commandText          the (non-{@code null}) input text, stripped from
	 *                             the command started symbol(s).
	 * @param trimmedCoreArguments the (non-{@code null}) {@link List} of all
	 *                             trimmed arguments of the command.
	 * @param channelId            the (non-{@code null}) ID of the channel of the
	 *                             originating post.
	 * @param userId               the (non-{@code null}) ID of the user of the
	 *                             originating post.
	 * @return the corresponding {@link SharedTasksCommand}.
	 */
	private SharedTasksCommand parseFrom(String commandText, List<String> trimmedCoreArguments, String channelId,
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
					try {
						MUser doneMattermostUser = findMattermostUserFromUserSpecification(userSpecification);
						if (doneMattermostUser != null) {
							doneUserId = doneMattermostUser.getId();
						} else {
							// The optional argument could not be understood as a username.
							return new ErrorCommand(commandText, "Unknown user \"" + userSpecification + "\".");
						}
					} catch (IOException exception) {
						return new ErrorCommand(commandText,
								"There was an issue while identifying Mattermost user \"" + userSpecification + "\".");
					}
				} else {
					doneUserId = userId;
				}
				return new DoneCommand(commandText, taskName, channelId, doneUserId);
			}
		}
		return new ErrorCommand(commandText, errorMessage.get());
	}

	/**
	 * Interprets a "user specification" text entered by a user into the
	 * corresponding Mattermost user. "User specifications" are expected to follow
	 * one of the forms below:
	 * <ul>
	 * <li>a username from the channel, e.g. 'stuart'</li>
	 * <li>a username from the channel, preceded by an '@', e.g. '@stuart'</li>
	 * </ul>
	 * 
	 * @param userSpecification the (non-{@code null}) user specification text
	 *                          entered by a user.
	 * @return the {@link MUser} corresponding to the input
	 *         {@code userSpecification}. {@code null} if none could be determined.
	 */
	private MUser findMattermostUserFromUserSpecification(String userSpecification) throws IOException {
		// First check whether the specification starts with '@' which is used for
		// highlighting in Mattermost syntax.
		String userNameSpecification;
		if (userSpecification.startsWith(MattermostSyntax.HIGHLIGHT)) {
			userNameSpecification = userSpecification.substring(1);
		} else {
			userNameSpecification = userSpecification;
		}

		// Search for a user using its username.
		Map<String, MUser> usersByUsername = this.bot
				.getUsersByUsername(Collections.singletonList(userNameSpecification));
		return usersByUsername.get(userNameSpecification);
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
		boolean correctVerb = ALL_VERBS_USAGE.keySet().stream()
				.filter(s -> s.toLowerCase().equals(taskName.toLowerCase())).findFirst().isPresent();
		if (!correctVerb) {
			issues.add("\"" + taskName + "\" is an not a valid verb. Please choose among: "
					+ ALL_VERBS_USAGE.keySet().stream().collect(Collectors.joining(", ")));
		}

		return issues;
	}
}
