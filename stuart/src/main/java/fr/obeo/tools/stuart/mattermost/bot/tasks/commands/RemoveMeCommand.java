package fr.obeo.tools.stuart.mattermost.bot.tasks.commands;

import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.CommandExecutionContext;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.CommandExecutionException;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.CommandWithTaskNameAndChannelIdAndUserId;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.SharedTasksCommand;

/**
 * {@link SharedTasksCommand} implementation for a user to remove themselves
 * from a task in a channel.
 * 
 * @author flatombe
 *
 */
public class RemoveMeCommand extends CommandWithTaskNameAndChannelIdAndUserId {

	/**
	 * Creates a new {@link RemoveMeCommand}.
	 * 
	 * @param commandText         the (non-{@code null}) textual form of the
	 *                            command.
	 * @param taskName            the (non-{@code null}) name of the task the user
	 *                            wants to remove themselves from.
	 * @param mattermostChannelId the (non-{@code null}) ID of the Mattermost
	 *                            channel concerned by this command.
	 * @param mattermostUserId    the (non-{@code null}) ID of the Mattermost user
	 *                            that wants to remove themselves from the task.
	 */
	public RemoveMeCommand(String commandText, String taskName, String mattermostChannelId, String mattermostUserId) {
		super(commandText, taskName, mattermostChannelId, mattermostUserId);
	}

	@Override
	public void execute(CommandExecutionContext commandExecutionContext) throws CommandExecutionException {
		// TODO: implement
		// 1. Find the sheet corresponding to the task.
		// 2. Check that the user is indeed registered for the task.
		// 3. Remove the user from the task.
		// 4. Consolidate the table by removing blank lines if necessary.
	}

}
