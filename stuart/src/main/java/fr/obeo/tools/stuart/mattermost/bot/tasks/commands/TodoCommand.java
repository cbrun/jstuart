package fr.obeo.tools.stuart.mattermost.bot.tasks.commands;

import java.io.IOException;
import java.time.Instant;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import fr.obeo.tools.stuart.mattermost.MattermostSyntax;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.CommandExecutionContext;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.CommandExecutionException;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.CommandWithTaskNameAndChannelId;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.SharedTasksCommand;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.SharedTasksCommandFactory;
import fr.obeo.tools.stuart.mattermost.bot.tasks.google.GoogleException;
import fr.obeo.tools.stuart.mattermost.bot.tasks.google.SharedTasksGoogleUtils;
import fr.obeo.tools.stuart.mattermost.bot.user.MUser;

/**
 * {@link SharedTasksCommand} implementation that indicates that a task of a
 * channel must be performed. One of the users registered for this task is
 * assigned the task and notified about it.
 * 
 * @author flatombe
 *
 */
public class TodoCommand extends CommandWithTaskNameAndChannelId {

	/**
	 * Creates a new {@link TodoCommand}.
	 * 
	 * @param commandText         the (non-{@code null}) textual form of the
	 *                            command.
	 * @param taskName            the (non-{@code null}) name of the task that must
	 *                            be done.
	 * @param mattermostChannelId the (non-{@code null}) ID of the Mattermost
	 *                            channel concerned by this command.
	 */
	public TodoCommand(String commandText, String taskName, String mattermostChannelId) {
		super(commandText, taskName, mattermostChannelId);
	}

	@Override
	public void execute(CommandExecutionContext commandExecutionContext) throws CommandExecutionException {
		Map<String, Instant> usersAndTheirTimestamp = this
				.getAllRegisteredUsersAndTheirTimestamps(commandExecutionContext);
		if (usersAndTheirTimestamp != null) {
			if (usersAndTheirTimestamp.isEmpty()) {
				try {
					commandExecutionContext.getBot().respond(commandExecutionContext.getPost(),
							"There are no registered users for this task, so it cannot be assigned to anyone. Use command "
									+ SharedTasksCommandFactory.COMMAND_STARTER + this.getTaskName()
									+ SharedTasksCommandFactory.COMMAND_SEPARATOR + SharedTasksCommandFactory.VERB_ADDME
									+ " to register yourself for this task.");
				} catch (IOException exception) {
					throw new CommandExecutionException(
							"There was an issue while responding for a task that has no registered users.", exception);
				}
			} else {
				// There is at least one user registered for the task.
				List<String> pastAssignedUsers = getPastAssignedUserIds(commandExecutionContext);
				String userIdToAssignTheTaskTo = determineNextUserToAssignTheTaskTo(usersAndTheirTimestamp,
						pastAssignedUsers);
				assignTaskToUser(commandExecutionContext, this.getTaskName(), this.getChannelId(),
						userIdToAssignTheTaskTo);
			}
		}
		// else it has already been handled because the task does not exist.
	}

	/**
	 * Selects the user to whom the task will be assigned.
	 * 
	 * @param usersAndTheirTimestamp the (non-{@code null}) {@link Map} of the
	 *                               registered user IDs and their timestamps.
	 * @param pastAssignedUsers      the (non-{@code null}) {@link List} of user IDs
	 *                               to whom the task has been assigned since it was
	 *                               lastt done.
	 * @return the user ID to whom the task must be assigned.
	 */
	private static String determineNextUserToAssignTheTaskTo(Map<String, Instant> usersAndTheirTimestamp,
			List<String> pastAssignedUsers) {
		String selectedUserId;

		// First try among those who have no timestamp and have not been assigned the
		// task already.
		List<String> newbiesId = usersAndTheirTimestamp.entrySet().stream()
				.filter(entry -> !pastAssignedUsers.contains(entry.getKey())).filter(entry -> entry.getValue() == null)
				.map(Map.Entry::getKey).collect(Collectors.toList());
		if (!newbiesId.isEmpty()) {
			selectedUserId = newbiesId.get(0);
		} else {
			// Else find the user with the oldest timestamp and to whom the task has not
			// been assigned already.
			Optional<String> unassignedUserIdWithOldestTimestamp = usersAndTheirTimestamp.entrySet().stream()
					.filter(entry -> !pastAssignedUsers.contains(entry.getKey()))
					.max(Comparator.comparing(entry -> entry.getValue())).map(entry -> entry.getKey());
			if (unassignedUserIdWithOldestTimestamp.isPresent()) {
				selectedUserId = unassignedUserIdWithOldestTimestamp.get();
			} else {
				// Could not find a newbie to whom the task has not been assigned, or a user to
				// whom the task has not been assigned.
				// So we try one more time to assign the task to someone to whom the task has
				// already been assigned.
				// Although in practice this should not happen too often.
				return determineNextUserToAssignTheTaskTo(usersAndTheirTimestamp, Collections.emptyList());
			}
		}
		return selectedUserId;
	}

	/**
	 * Assigns a task to a user.
	 * 
	 * @param commandExecutionContext the (non-{@code null})
	 *                                {@link CommandExecutionContext}.
	 * @param taskName                the (non-{@code null}) name of the task.
	 * @param mattermostChannelId     the (non-{@code null}) ID of the Mattermost
	 *                                channel.
	 * @param userIdToAssignTheTaskTo the (non-{@code null}) ID of the Mattermost
	 *                                user to assign the task to.
	 * @throws CommandExecutionException
	 */
	private void assignTaskToUser(CommandExecutionContext commandExecutionContext, String taskName,
			String mattermostChannelId, String userIdToAssignTheTaskTo) throws CommandExecutionException {
		try {
			SharedTasksGoogleUtils.assignTaskToUser(commandExecutionContext.getSharedTasksSheetId(), taskName,
					mattermostChannelId, userIdToAssignTheTaskTo);
		} catch (GoogleException exception) {
			throw new CommandExecutionException("There was an issue while assigning task \"" + taskName
					+ "\" to user \"" + userIdToAssignTheTaskTo + "\".", exception);
		}

		MUser matterMostUserToAssignTheTaskTo = this
				.getUsersById(commandExecutionContext, Collections.singletonList(userIdToAssignTheTaskTo))
				.get(userIdToAssignTheTaskTo);
		String message = "Task \"" + taskName + "\" has been affected to " + MattermostSyntax.HIGHLIGHT
				+ matterMostUserToAssignTheTaskTo.getUsername() + ".";
		try {
			commandExecutionContext.getBot().respond(commandExecutionContext.getPost(), message);
		} catch (IOException exception) {
			throw new CommandExecutionException("There was an issue while responding for task \"" + taskName
					+ "\" being assigned to user \"" + userIdToAssignTheTaskTo + "\".", exception);
		}

	}

}
