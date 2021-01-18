package fr.obeo.tools.stuart.mattermost.bot.tasks.commands;

import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.SharedTasksCommand;

/**
 * {@link SharedTasksCommand} implementation for assigning a task to someone
 * else.
 * 
 * @author flatombe
 *
 */
public class RerollCommand extends TodoCommand {

	/**
	 * Creates a new {@link RerollCommand}.
	 * 
	 * @param commandText         the (non-{@code null}) textual form of the
	 *                            command.
	 * @param taskName            the (non-{@code null}) name of the task for which
	 *                            to reroll.
	 * @param mattermostChannelId the (non-{@code null}) ID of the Mattermost
	 *                            channel concerned by this command.
	 */
	public RerollCommand(String commandText, String taskName, String mattermostChannelId) {
		super(commandText, taskName, mattermostChannelId);
	}

}
