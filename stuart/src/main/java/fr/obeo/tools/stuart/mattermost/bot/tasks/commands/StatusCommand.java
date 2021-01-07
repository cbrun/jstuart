package fr.obeo.tools.stuart.mattermost.bot.tasks.commands;

import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.CommandExecutionContext;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.CommandExecutionException;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.CommandWithTaskNameAndChannelId;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.SharedTasksCommand;

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
		// TODO: implement
		// 1. Find the sheet corresponding to the task.
		// 2. Display some information about the task, like the last time it was done
		// (possibly xx days since...), by whom and how many people are registered for
		// this task.
	}

}
