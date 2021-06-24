package fr.obeo.tools.stuart.mattermost.bot.tasks.commands;

import java.io.IOException;

import fr.obeo.tools.stuart.mattermost.MattermostUtils;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.CommandDocumentation;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.CommandExecutionContext;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.CommandExecutionException;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.CommandWithTaskNameAndUserId;
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
public class DoneCommand extends CommandWithTaskNameAndUserId {
	public static CommandDocumentation DOCUMENTATION = new CommandDocumentation() {

		@Override
		public String getPurpose() {
			return "Marks the task as done, by the caller (default), or by someone else (using the argument)";
		};

		@Override
		public String getUsage(String... commandArguments) {
			final String taskName = (commandArguments != null && commandArguments.length > 0) ? commandArguments[0]
					: "<task name>";
			final String userName = (commandArguments != null && commandArguments.length > 1) ? commandArguments[1]
					: "(<user name>)";
			return SharedTasksCommandFactory.COMMAND_STARTER + SharedTasksCommandFactory.VERB_DONE
					+ SharedTasksCommandFactory.COMMAND_SEPARATOR + taskName
					+ SharedTasksCommandFactory.COMMAND_SEPARATOR + userName;
		};
	};

	/**
	 * Creates a new {@link DoneCommand}.
	 * 
	 * @param commandText         the (non-{@code null}) textual form of the
	 *                            command.
	 * @param mattermostChannelId the (non-{@code null}) ID of the Mattermost
	 *                            channel concerned by this command.
	 * @param taskName            the (non-{@code null}) name of the task that has
	 *                            been done.
	 * @param mattermostUserId    the (non-{@code null}) ID of the Mattermost user
	 *                            who did the task.
	 */
	public DoneCommand(String commandText, String mattermostChannelId, String taskName, String mattermostUserId) {
		super(commandText, mattermostChannelId, taskName, mattermostUserId);
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
					+ (doneUserIsRegistered ? "" : MattermostUtils.HIGHLIGHT) + doneByUser.getUsername() + ".";
			if (!doneUserIsRegistered) {
				successMessage += " To register yourself for this task, use command ```"
						+ SharedTasksCommandFactory.ALL_VERBS_DOCUMENTATION.get(SharedTasksCommandFactory.VERB_ADDME)
								.getUsage(this.getTaskName())
						+ "```.";
			}

			commandExecutionContext.getBot().respond(commandExecutionContext.getPost(), successMessage);
		} catch (GoogleException | IOException exception) {
			throw new CommandExecutionException("There was an issue while setting task \"" + this.getTaskName()
					+ "\" as done by user \"" + this.getUserId() + "\".", exception);
		}
	}
}
