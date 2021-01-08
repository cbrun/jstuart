package fr.obeo.tools.stuart.mattermost.bot.tasks.commands;

import java.io.IOException;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.CommandExecutionContext;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.CommandExecutionException;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.CommandWithTaskNameAndChannelId;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.SharedTasksCommand;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.SharedTasksCommandFactory;

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
		// TODO: implement
		// 1. Find the sheet for the task.
		// 2. Determine a user to assign the task to.
		// 3. Notify the user and mark it on the sheet.
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
				String selectedUserId;
				// First try among those who have no timestamp
				List<String> newbiesId = usersAndTheirTimestamp.entrySet().stream()
						.filter(entry -> entry.getValue() == null).map(Map.Entry::getKey).collect(Collectors.toList());
				if (!newbiesId.isEmpty()) {
					selectedUserId = newbiesId.get(0);
				} else {
					// Else find the oldest timestamp
					selectedUserId = usersAndTheirTimestamp.entrySet().stream()
							.max(Comparator.comparing(entry -> entry.getValue())).get().getKey();
				}
				// TODO: mark the selected user ID in the third column at the first non-blank
				// row.

				// TODO: find how to do userID <-> username association
				String message = "Task has been affected to user " + selectedUserId;
				try {
					commandExecutionContext.getBot().respond(commandExecutionContext.getPost(), message);
				} catch (IOException exception) {
					throw new CommandExecutionException("There was an issue while notifying a user for a task.",
							exception);
				}
			}
		}
		// else it has already been handled because the task does not exist.
	}

}
