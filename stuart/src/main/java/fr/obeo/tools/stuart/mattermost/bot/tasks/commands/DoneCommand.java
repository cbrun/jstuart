package fr.obeo.tools.stuart.mattermost.bot.tasks.commands;

import java.io.IOException;

import fr.obeo.tools.stuart.mattermost.MattermostSyntax;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.CommandExecutionContext;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.CommandExecutionException;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.CommandWithTaskNameAndChannelIdAndUserId;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.SharedTasksCommand;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.SharedTasksCommandFactory;
import fr.obeo.tools.stuart.mattermost.bot.tasks.google.GoogleException;
import fr.obeo.tools.stuart.mattermost.bot.tasks.google.SharedTasksGoogleUtils;
import fr.obeo.tools.stuart.mattermost.bot.user.MUser;

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
			SharedTasksGoogleUtils.markTaskAsDone(commandExecutionContext.getSharedTasksSheetId(), this.getTaskName(),
					this.getChannelId(), this.getUserId());

			MUser doneByUser = this.getMattermostUser(commandExecutionContext);
			boolean doneUserIsRegistered = this.getAllRegisteredUserIds(commandExecutionContext)
					.contains(doneByUser.getId());

			// If the task was done by someone who is not registered for it, highlight them
			// to tell them about adding themselves to the task.
			String successMessage = "Task \"" + this.getTaskName() + "\" marked as done by "
					+ (doneUserIsRegistered ? "" : MattermostSyntax.HIGHLIGHT) + doneByUser.getUsername() + ".";
			if (!doneUserIsRegistered) {
				successMessage += " To register yourself for this task, use command \""
						+ SharedTasksCommandFactory.ALL_VERBS_USAGE.get(SharedTasksCommandFactory.VERB_ADDME)
								.apply(this.getTaskName())
						+ "\".";
			}

			commandExecutionContext.getBot().respond(commandExecutionContext.getPost(), successMessage);
		} catch (GoogleException | IOException exception) {
			throw new CommandExecutionException("There was an issue while setting task \"" + this.getTaskName()
					+ "\" as done by user \"" + this.getUserId() + "\".", exception);
		}
	}
}
