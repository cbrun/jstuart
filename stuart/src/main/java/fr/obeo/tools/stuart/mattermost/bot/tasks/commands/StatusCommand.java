package fr.obeo.tools.stuart.mattermost.bot.tasks.commands;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.api.services.sheets.v4.model.Sheet;

import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.CommandDocumentation;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.CommandExecutionContext;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.CommandExecutionException;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.CommandWithTaskName;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.SharedTasksCommand;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.SharedTasksCommandFactory;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.SharedTasksCommandFactory.SharedTasksVerb;
import fr.obeo.tools.stuart.mattermost.bot.tasks.google.GoogleException;
import fr.obeo.tools.stuart.mattermost.bot.tasks.google.SharedTasksGoogleUtils;
import fr.obeo.tools.stuart.mattermost.bot.user.MUser;

/**
 * {@link SharedTasksCommand} implementation for displaying the current status
 * of a task in a channel.
 * 
 * @author flatombe
 *
 */
public class StatusCommand extends CommandWithTaskName {
	public static CommandDocumentation DOCUMENTATION = new CommandDocumentation() {

		@Override
		public String getPurpose() {
			return "Displays information about the task (current state, last done timestamp, etc.)";
		};

		@Override
		public String getUsage(String... commandArguments) {
			final String taskName = (commandArguments != null && commandArguments.length > 0) ? commandArguments[0]
					: "<task name>";
			return SharedTasksCommandFactory.COMMAND_STARTER + SharedTasksVerb.STATUS.getLabel()
					+ SharedTasksCommandFactory.COMMAND_SEPARATOR + taskName;
		};
	};

	/**
	 * Creates a new {@link StatusCommand}.
	 * 
	 * @param commandText         the (non-{@code null}) textual form of the
	 *                            command.
	 * @param mattermostChannelId the (non-{@code null}) ID of the Mattermost
	 *                            channel concerned by this command.
	 * @param taskName            the (non-{@code null}) name of the task for which
	 *                            we want the status.
	 */
	public StatusCommand(String commandText, String mattermostChannelId, String taskName) {
		super(commandText, mattermostChannelId, taskName);
	}

	@Override
	public void execute(CommandExecutionContext commandExecutionContext) throws CommandExecutionException {

		Sheet taskSheet = this.getExistingTaskSheetElseRespondWithMessage(commandExecutionContext);
		if (taskSheet != null) {
			try {
				String statusMessage = "Status of task \"" + this.getTaskName() + "\":";

				// The most recent users to whom the task has been assigned.
				List<String> pastAssignedUserIds = getPastAssignedUserIds(commandExecutionContext);
				if (pastAssignedUserIds != null && !pastAssignedUserIds.isEmpty()) {
					Map<String, MUser> pastAssignedUsersById = this.getUsersById(commandExecutionContext,
							pastAssignedUserIds);
					List<String> pastAssignedUserNames = pastAssignedUserIds.stream()
							.map(userId -> pastAssignedUsersById.get(userId).getUsername())
							.collect(Collectors.toList());
					statusMessage += "\n* Currently assigned to: `"
							+ pastAssignedUserNames.get(pastAssignedUserNames.size() - 1) + "`";
				}
				
				// determine the next user
				Map<String, Instant> usersAndTheirTimestamp = this
						.getAllRegisteredUsersAndTheirTimestamps(commandExecutionContext);
				List<String> pastAssignedUsers = getPastAssignedUserIds(commandExecutionContext);
				String userIdToAssignTheTaskTo = TodoCommand.determineNextUserToAssignTheTaskTo(usersAndTheirTimestamp,
						pastAssignedUsers);
				String nextUserName = this
						.getUsersById(commandExecutionContext, Collections.singletonList(userIdToAssignTheTaskTo))
						.get(userIdToAssignTheTaskTo).getUsername();
				statusMessage += "\n*  Next user: " + "`" + nextUserName + "`";

				// The last time the task has been done.
				Map<Instant, String> history = SharedTasksGoogleUtils.getTaskRealizationHistory(
						commandExecutionContext.getSharedTasksSheetId(), this.getTaskName(), this.getChannelId());
				Map.Entry<Instant, String> lastDone = history.entrySet().stream()
						.max(Comparator.comparing(entry -> entry.getKey())).orElse(null);
				if (lastDone != null) {
					Instant lastDoneTime = lastDone.getKey();
					String lastDoneUserId = lastDone.getValue();
					String lastDoneUserName = this
							.getUsersById(commandExecutionContext, Collections.singletonList(lastDoneUserId))
							.get(lastDoneUserId).getUsername();
					LocalDateTime localDoneTime = LocalDateTime.ofInstant(lastDoneTime, ZoneId.of("Europe/Paris"));

					statusMessage += "\n* Last done on " + TIME_FORMATTER.format(localDoneTime) + " by `"
							+ lastDoneUserName + "`.";
				}

				// The number of registered users.
				List<String> registeredUserIds = new ArrayList(usersAndTheirTimestamp.keySet());
				String registeredUsers = getUsersById(commandExecutionContext, registeredUserIds).values().stream()
						.map(user -> "`" + user.getUsername() + "`").collect(Collectors.joining(","));
				statusMessage += "\n*  Registered users: " + registeredUsers;

				try {
					commandExecutionContext.getBot().respond(commandExecutionContext.getPost(), statusMessage);
				} catch (IOException exception) {
					throw new CommandExecutionException(
							"There was an issue while responding with the status message for task \""
									+ this.getTaskName() + "\".",
							exception);
				}
			} catch (GoogleException exception) {
				throw new CommandExecutionException(
						"There was an issue while retrieving the realization history for task \"" + this.getTaskName()
								+ "\".",
						exception);
			}
		}
		// else it has already been handled because the task does not exist.
	}

}
