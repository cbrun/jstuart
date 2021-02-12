package fr.obeo.tools.stuart.mattermost.bot.tasks.commands;

import java.io.IOException;
import java.time.Instant;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.api.services.sheets.v4.model.Sheet;

import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.CommandExecutionContext;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.CommandExecutionException;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.CommandWithTaskNameAndChannelId;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.SharedTasksCommand;
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
public class StatusCommand extends CommandWithTaskNameAndChannelId {

	/**
	 * Creates a new {@link StatusCommand}.
	 * 
	 * @param commandText         the (non-{@code null}) textual form of the
	 *                            command.
	 * @param taskName            the (non-{@code null}) name of the task for which
	 *                            we want the status.
	 * @param mattermostChannelId the (non-{@code null}) ID of the Mattermost
	 *                            channel concerned by this command.
	 */
	public StatusCommand(String commandText, String taskName, String mattermostChannelId) {
		super(commandText, taskName, mattermostChannelId);
	}

	@Override
	public void execute(CommandExecutionContext commandExecutionContext) throws CommandExecutionException {
		Sheet taskSheet = this.getExistingTaskSheetElseRespondWithMessage(commandExecutionContext);
		if (taskSheet != null) {
			int numberOfRegisteredUsers = this.getAllRegisteredUserIds(commandExecutionContext).size();
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
					statusMessage += "\n* Last assigned to:"
							+ pastAssignedUserNames.stream().collect(Collectors.joining(", "));
				}

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
					statusMessage += "\n* Last done on " + lastDoneTime.toString() + " by " + lastDoneUserName + ".";
				}

				// The number of registered users.
				statusMessage += "\n* " + numberOfRegisteredUsers + " registered users.";

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
