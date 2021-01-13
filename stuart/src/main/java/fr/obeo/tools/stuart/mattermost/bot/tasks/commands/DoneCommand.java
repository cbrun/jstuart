package fr.obeo.tools.stuart.mattermost.bot.tasks.commands;

import java.io.IOException;

import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.CommandExecutionContext;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.CommandExecutionException;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.CommandWithTaskNameAndChannelIdAndUserId;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.SharedTasksCommand;
import fr.obeo.tools.stuart.mattermost.bot.tasks.google.GoogleException;
import fr.obeo.tools.stuart.mattermost.bot.tasks.google.SharedTasksGoogleUtils;

/**
 * {@link SharedTasksCommand} implementation to indicate that a task has been
 * done.
 * 
 * @author lfasani
 *
 */
public class DoneCommand extends CommandWithTaskNameAndChannelIdAndUserId {

	/**
	 * Creates a new {@link DoneCommand}.
	 * 
	 * @param commandText         the (non-{@code null}) textual form of the
	 *                            command.
	 * @param taskName            the (non-{@code null}) name of the task that has
	 *                            been done.
	 * @param mattermostChannelId the (non-{@code null}) ID of the Mattermost
	 *                            channel concerned by this command.
	 * @param mattermostUserId    the (non-{@code null}) ID of the Mattermost user
	 *                            who did the task.
	 */
	public DoneCommand(String commandText, String taskName, String mattermostChannelId, String mattermostUserId) {
		super(commandText, taskName, mattermostChannelId, mattermostUserId);
	}

	@Override
	public void execute(CommandExecutionContext commandExecutionContext) throws CommandExecutionException {
		try {
			SharedTasksGoogleUtils.doTask(commandExecutionContext.getSharedTasksSheetId(), this.getTaskName(),
					this.getChannelId(), this.getUserId());

			String successMessage = "The task \"" + this.getTaskName() + "\" has been successfully done by \""
					+ this.getUserId() + "\".";
			commandExecutionContext.getBot().respond(commandExecutionContext.getPost(), successMessage);
		} catch (GoogleException | IOException exception) {
			throw new CommandExecutionException("There was an issue while setting task \"" + this.getTaskName()
					+ "\" as done by user \"" + this.getUserId() + "\".", exception);
		}
	}
}
