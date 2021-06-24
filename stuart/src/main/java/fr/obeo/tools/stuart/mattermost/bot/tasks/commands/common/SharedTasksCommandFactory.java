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
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;

import fr.obeo.tools.stuart.mattermost.MattermostUtils;
import fr.obeo.tools.stuart.mattermost.bot.MMBot;
import fr.obeo.tools.stuart.mattermost.bot.MPost;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.AddMeCommand;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.CreateTaskCommand;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.DoneCommand;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.ErrorCommand;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.HelpCommand;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.RemoveMeCommand;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.StatusCommand;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.TodoCommand;
import fr.obeo.tools.stuart.mattermost.bot.tasks.google.GoogleUtils;
import fr.obeo.tools.stuart.mattermost.bot.tasks.google.SharedTasksGoogleUtils;
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

	/**
	 * Used by the parser to find keywords and arguments.
	 */
	public final static String COMMAND_SEPARATOR = " ";

	// TODO: we may need a proper enum with all our known instances.
	public static final String VERB_STATUS = "status";
	public static final String VERB_CREATE = "create";
	public static final String VERB_ADDME = "addMe";
	public static final String VERB_REMOVEME = "removeMe";
	public static final String VERB_TODO = "todo";
	public static final String VERB_DONE = "done";
	public static final String VERB_HELP = "help";

	/**
	 * This {@link Map} centralizes all verbs that may be used for keyword
	 * {@link #KEYWORD_TASKS}. Associated to each verb is a
	 * {@link CommandDocumentation} that provides a user-facing information to show
	 * the usage and documentation of the verb for a particular task name.
	 */
	public static final Map<String, CommandDocumentation> ALL_VERBS_DOCUMENTATION = createVerbDocumentationMap();

	private static Map<String, CommandDocumentation> createVerbDocumentationMap() {
		Map<String, CommandDocumentation> verbToDocumentation = new LinkedHashMap<>();
		verbToDocumentation.put(VERB_HELP, HelpCommand.DOCUMENTATION);
		verbToDocumentation.put(VERB_CREATE, CreateTaskCommand.DOCUMENTATION);
		verbToDocumentation.put(VERB_STATUS, StatusCommand.DOCUMENTATION);
		verbToDocumentation.put(VERB_ADDME, AddMeCommand.DOCUMENTATION);
		verbToDocumentation.put(VERB_REMOVEME, RemoveMeCommand.DOCUMENTATION);
		verbToDocumentation.put(VERB_TODO, TodoCommand.DOCUMENTATION);
		verbToDocumentation.put(VERB_DONE, DoneCommand.DOCUMENTATION);
		return verbToDocumentation;
	}

	private static final String KEYWORD_TASKS = "task";

	/**
	 * The maximum length authorized for a task name.
	 */
	private static final int TASK_NAME_MAXIMUM_LENGTH = GoogleUtils.SHEET_NAME_MAXIMUM_LENGTH
			- (MattermostUtils.CHANNEL_ID_LENGTH + SharedTasksGoogleUtils.TASK_SHEET_TITLE_SEPARATOR.length());

	private final MMBot bot;

	public SharedTasksCommandFactory(MMBot bot) {
		this.bot = bot;
	}

	/**
	 * Attempts to parse a {@link MPost Mattermost post} into a
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
			return new ErrorCommand(commandText, channelId, "Empty text cannot be parsed as a command");
		} else {
			if (trimmedArguments.get(0).equalsIgnoreCase(KEYWORD_TASKS)) {
				return parseFrom(commandText, channelId, trimmedArguments.subList(1, trimmedArguments.size()), userId);
			} else {
				return parseFrom(commandText, channelId, trimmedArguments, userId);
			}
		}
	}

	/**
	 * Parses the text entered by a user that corresponds to a command into a
	 * {@link SharedTasksCommand}.
	 * 
	 * @param commandText          the (non-{@code null}) input text, stripped from
	 *                             the command started symbol(s).
	 * @param channelId            the (non-{@code null}) ID of the channel of the
	 *                             originating post.
	 * @param trimmedCoreArguments the (non-{@code null}) {@link List} of all
	 *                             trimmed arguments of the command.
	 * @param userId               the (non-{@code null}) ID of the user of the
	 *                             originating post.
	 * @return the corresponding {@link SharedTasksCommand}.
	 */
	private SharedTasksCommand parseFrom(String commandText, String channelId, List<String> trimmedCoreArguments,
			String userId) {
		// TODO we should delegate the error management to the concrete command rather
		// doing it here
		Optional<String> errorMessage = getIssuesWithCommand(trimmedCoreArguments);
		if (!errorMessage.isPresent()) {
			String verbLiteral = trimmedCoreArguments.get(0);
			if (verbLiteral.equalsIgnoreCase(VERB_HELP)) {
				return new HelpCommand(commandText, channelId);
			} else {
				String taskName = trimmedCoreArguments.get(1);
				if (verbLiteral.equalsIgnoreCase(VERB_STATUS)) {
					return new StatusCommand(commandText, channelId, taskName);
				} else if (verbLiteral.equalsIgnoreCase(VERB_CREATE)) {
					return new CreateTaskCommand(commandText, channelId, taskName);
				} else if (verbLiteral.equalsIgnoreCase(VERB_ADDME)) {
					return new AddMeCommand(commandText, channelId, taskName, userId);
				} else if (verbLiteral.equalsIgnoreCase(VERB_REMOVEME)) {
					return new RemoveMeCommand(commandText, channelId, taskName, userId);
				} else if (verbLiteral.equalsIgnoreCase(VERB_TODO)) {
					return new TodoCommand(commandText, channelId, taskName);
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
								return new ErrorCommand(commandText, channelId,
										"Unknown user \"" + userSpecification + "\".");
							}
						} catch (IOException exception) {
							return new ErrorCommand(commandText, channelId,
									"There was an issue while identifying Mattermost user \"" + userSpecification
											+ "\".");
						}
					} else {
						doneUserId = userId;
					}
					return new DoneCommand(commandText, channelId, taskName, doneUserId);
				}
			}
		}
		return new ErrorCommand(commandText, channelId, errorMessage.get());
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
		if (userSpecification.startsWith(MattermostUtils.HIGHLIGHT)) {
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

		if (trimmedCoreArguments.size() < 1) {
			issues.add("Invalid command. Try one of the following verbs: "
					+ ALL_VERBS_DOCUMENTATION.keySet().stream().collect(Collectors.joining(", ")) + ".");
		} else {
			String verbName = trimmedCoreArguments.get(0);
			issues.addAll(getIssuesWithVerbName(verbName));

			if (trimmedCoreArguments.size() > 1) {
				String taskName = trimmedCoreArguments.get(1);
				issues.addAll(getIssuesWithTaskName(taskName));
			}
		}

		String errorMessage = issues.stream().collect(Collectors.joining("\n"));
		return issues.isEmpty() ? Optional.empty() : Optional.of(errorMessage);
	}

	private static List<String> getIssuesWithTaskName(String taskName) {
		List<String> issues = new ArrayList<>();

		if (!StringUtils.isAlphanumeric(taskName)) {
			// Hinder users from using Mattermost-syntax characters for their task names by
			// allowing only alpha-numeric names.
			issues.add("Task name must be alpha-numeric.");
		}

		if (taskName.equalsIgnoreCase(KEYWORD_TASKS)) {
			// Otherwise it will clash with our keyword.
			issues.add("Task name may not be \"" + KEYWORD_TASKS + "\".");
		}

		boolean incorrectVerb = ALL_VERBS_DOCUMENTATION.keySet().stream()
				.filter(s -> s.toLowerCase().equals(taskName.toLowerCase())).findFirst().isPresent();
		if (incorrectVerb) {
			issues.add("\"" + taskName + "\" can not be named like a verb");
		}

		if (taskName.length() > TASK_NAME_MAXIMUM_LENGTH) {
			// Otherwise we won't be able to create the corresponding sheet.
			issues.add("Task name may not have more than " + TASK_NAME_MAXIMUM_LENGTH + " characters.");
		}

		return issues;
	}

	private static List<String> getIssuesWithVerbName(String verbName) {
		List<String> issues = new ArrayList<>();
		boolean correctVerb = ALL_VERBS_DOCUMENTATION.keySet().stream()
				.filter(s -> s.toLowerCase().equals(verbName.toLowerCase())).findFirst().isPresent();
		if (!correctVerb) {
			issues.add("\"" + verbName + "\" is an not a valid verb. Please choose among: "
					+ ALL_VERBS_DOCUMENTATION.keySet().stream().collect(Collectors.joining(", ")));
		}

		return issues;
	}
}
