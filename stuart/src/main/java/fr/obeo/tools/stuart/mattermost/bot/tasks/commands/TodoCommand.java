package fr.obeo.tools.stuart.mattermost.bot.tasks.commands;

import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.CommandExecutionContext;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.CommandExecutionException;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.CommandWithTaskNameAndChannelId;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.SharedTasksCommand;

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
	}

}
