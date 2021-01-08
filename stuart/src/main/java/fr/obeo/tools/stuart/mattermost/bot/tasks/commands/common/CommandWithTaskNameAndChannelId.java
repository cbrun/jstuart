package fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common;

import java.io.IOException;

import com.google.api.services.sheets.v4.model.Sheet;

import fr.obeo.tools.stuart.mattermost.bot.tasks.google.GoogleException;
import fr.obeo.tools.stuart.mattermost.bot.tasks.google.SharedTasksGoogleUtils;

/**
 * Partial {@link SharedTasksCommand} implementation that holds a task name and
 * a mattermost channel ID.
 * 
 * @author flatombe
 *
 */
public abstract class CommandWithTaskNameAndChannelId extends SharedTasksCommand {

	private final String taskName;

	private final String channelId;

	/**
	 * Creates a new {@link CommandWithTaskNameAndChannelId}.
	 * 
	 * @param commandText         the (non-{@code null}) textual form of the
	 *                            command.
	 * @param taskName            the (non-{@code null}) name of the task concerned
	 *                            by this command.
	 * @param mattermostChannelId the (non-{@code null}) ID of the channel concerned
	 *                            by this command.
	 */
	public CommandWithTaskNameAndChannelId(String commandText, String taskName, String mattermostChannelId) {
		super(commandText);
		this.taskName = taskName;
		this.channelId = mattermostChannelId;
	}

	/**
	 * Provides the name of the task concerned by this command.
	 * 
	 * @return the (non-{@code null}) task name as typed by the user when creating
	 *         this command.
	 */
	public String getTaskName() {
		return this.taskName;
	}

	/**
	 * Provides the ID of the Mattermost channel concerned by this command.
	 * 
	 * @return the (non-{@code null}) ID of the Mattermost channel.
	 */
	public String getChannelId() {
		return this.channelId;
	}

	/**
	 * Provides the {@link Sheet} corresponding to the task concerned by this
	 * command.
	 * 
	 * @param commandExecutionContext the (non-{@code null})
	 *                                {@link CommandExecutionContext}.
	 * @return the {@link Sheet} in the spreadsheet corresponding to the task
	 *         concerned by this command. {@code null} if no such sheet exists in
	 *         the spreadsheet.
	 * @throws CommandExecutionException
	 */
	protected Sheet getTaskSheet(CommandExecutionContext commandExecutionContext) throws CommandExecutionException {
		try {
			return SharedTasksGoogleUtils.getSheetForTask(commandExecutionContext.getSharedTasksSheetId(),
					this.getTaskName(), this.getChannelId());
		} catch (GoogleException exception) {
			throw new CommandExecutionException(exception);
		}
	}

	/**
	 * Provides the {@link Sheet} corresponding to the task concerned by this
	 * command. If it does not already exist, then the bot responds with a message
	 * indicating so.
	 * 
	 * @param commandExecutionContext the (non-{@code null})
	 *                                {@link CommandExecutionContext}.
	 * @return the {@link Sheet} corresponding to the task concerned by this
	 *         command. {@code null} if it does not exist.
	 * @throws CommandExecutionException
	 */
	protected Sheet getExistingTaskSheetElseRespondWithMessage(CommandExecutionContext commandExecutionContext)
			throws CommandExecutionException {
		Sheet taskSheet = this.getTaskSheet(commandExecutionContext);
		if (taskSheet == null) {
			String failureMessage = "Task \"" + this.getTaskName()
					+ "\" does not exist. You may want to create it using command \"";
			try {
				commandExecutionContext.getBot().respond(commandExecutionContext.getPost(), failureMessage);
			} catch (IOException exception) {
				throw new CommandExecutionException(
						"There was an issue while sending a message about the non-existence of task \""
								+ this.getTaskName() + "\".",
						exception);
			}
			return null;
		} else {
			return taskSheet;
		}
	}
}
