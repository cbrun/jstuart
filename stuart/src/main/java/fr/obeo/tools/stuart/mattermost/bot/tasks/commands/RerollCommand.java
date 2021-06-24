package fr.obeo.tools.stuart.mattermost.bot.tasks.commands;

import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.CommandWithTaskName;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.SharedTasksCommand;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.SharedTasksCommandFactory;

/**
 * {@link SharedTasksCommand} implementation for assigning a task to someone
 * else.
 * 
 * TODO: remove as this is redundant with {@link TodoCommand}.
 * 
 * @author flatombe
 *
 */
public class RerollCommand extends TodoCommand {

	public static CommandWithTaskName.CommandInformation INFORMATION = new CommandWithTaskName.CommandInformation() {
		public String getDocumentation() {
			return "Re-assigns a task that has to be done";
		};

		public String getUsage(String taskName) {
			return SharedTasksCommandFactory.COMMAND_STARTER + SharedTasksCommandFactory.VERB_REROLL
					+ SharedTasksCommandFactory.COMMAND_SEPARATOR + (taskName != null ? taskName : "<task name>");
		};
	};

	/**
	 * Creates a new {@link RerollCommand}.
	 * 
	 * @param commandText         the (non-{@code null}) textual form of the
	 *                            command.
	 * @param mattermostChannelId the (non-{@code null}) ID of the Mattermost
	 *                            channel concerned by this command.
	 * @param taskName            the (non-{@code null}) name of the task for which
	 *                            to reroll.
	 */
	public RerollCommand(String commandText, String mattermostChannelId, String taskName) {
		super(commandText, mattermostChannelId, taskName);
	}

}
