package fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import com.google.api.services.sheets.v4.model.Sheet;

import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.SharedTasksCommandFactory.SharedTasksVerb;
import fr.obeo.tools.stuart.mattermost.bot.tasks.google.GoogleException;
import fr.obeo.tools.stuart.mattermost.bot.tasks.google.SharedTasksGoogleUtils;

/**
 * Partial {@link SharedTasksCommand} implementation that holds a task name.
 * 
 * @author flatombe
 *
 */
public abstract class CommandWithTaskName extends SharedTasksCommand {

	private final String taskName;

	/**
	 * Creates a new {@link CommandWithTaskName}.
	 * 
	 * @param commandText         the (non-{@code null}) textual form of the
	 *                            command.
	 * @param mattermostChannelId the (non-{@code null}) ID of the channel concerned
	 *                            by this command.
	 * @param taskName            the (non-{@code null}) name of the task concerned
	 *                            by this command.
	 */
	public CommandWithTaskName(String commandText, String mattermostChannelId, String taskName) {
		super(commandText, mattermostChannelId);
		this.taskName = taskName;
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
					+ "\" does not exist. You may want to create it using command \""
					+ SharedTasksVerb.CREATE.getDocumentation().getUsage(this.getTaskName()) + "\".";
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

	/**
	 * Provides the IDs of all the registered users for the task concerned by this
	 * command (if the task exists).
	 * 
	 * @param commandExecutionContext the (non-{@code null})
	 *                                {@link CommandExecutionContext}.
	 * @return the {@link List} of the IDs of all the registered users for the task
	 *         concerned by this command, or {@code null} if the task does not
	 *         exist.
	 * @throws CommandExecutionException
	 */
	protected List<String> getAllRegisteredUserIds(CommandExecutionContext commandExecutionContext)
			throws CommandExecutionException {
		Sheet taskSheet = this.getExistingTaskSheetElseRespondWithMessage(commandExecutionContext);
		if (taskSheet != null) {
			List<String> registeredUserIds;
			try {
				registeredUserIds = SharedTasksGoogleUtils.getAllRegisteredUserIds(
						commandExecutionContext.getSharedTasksSheetId(), this.getTaskName(), this.getChannelId());
				return registeredUserIds;
			} catch (GoogleException exception) {
				throw new CommandExecutionException(
						"There was an issue while retrieving the users registered for task \"" + this.getTaskName()
								+ "\".",
						exception);
			}
		} else {
			return null;
		}
	}

	/**
	 * Provides the map of all registered users (identified via their Mattermost ID)
	 * for the task concerned by this command and their associated timestamp which
	 * represents the last time they performed this task.
	 * 
	 * @param commandExecutionContext the (non-{@code null})
	 *                                {@link CommandExecutionContext}.
	 * @return the {@link Map} of the registered user IDs and their associated
	 *         {@link Instant timestamp}, or {@code null} if the task does not
	 *         exist.
	 * @throws CommandExecutionException
	 */
	protected Map<String, Instant> getAllRegisteredUsersAndTheirTimestamps(
			CommandExecutionContext commandExecutionContext) throws CommandExecutionException {
		Sheet taskSheet = this.getExistingTaskSheetElseRespondWithMessage(commandExecutionContext);
		if (taskSheet != null) {
			try {
				return SharedTasksGoogleUtils.getAllRegisteredUsersAndTheirTimestamps(
						commandExecutionContext.getSharedTasksSheetId(), this.getTaskName(), this.getChannelId());
			} catch (GoogleException exception) {
				throw new CommandExecutionException(
						"There was an issue while retrieving the users registered for task \"" + this.getTaskName()
								+ "\".",
						exception);
			}
		} else {
			return null;
		}
	}

	/**
	 * Provides the IDs of the Mattermost users to whom this task has been assigned
	 * since the last time it was done.
	 * 
	 * @param commandExecutionContext the (non-{@code null})
	 *                                {@link CommandExecutionContext}.
	 * @return the (non-{@code null}) {@link List} of the Mattermost user IDs to
	 *         whom the task has been assigned since it was last done. It may hold
	 *         duplicates.
	 * @throws CommandExecutionException
	 */
	protected List<String> getPastAssignedUserIds(CommandExecutionContext commandExecutionContext)
			throws CommandExecutionException {
		try {
			List<String> pastAssignedUsers = SharedTasksGoogleUtils.getAllAssignedUsersSinceLastDoneForTask(
					commandExecutionContext.getSharedTasksSheetId(), this.getTaskName(), this.getChannelId());
			return pastAssignedUsers;
		} catch (GoogleException exception) {
			throw new CommandExecutionException("There was an issue while retrieving the users assigned to task \""
					+ this.getTaskName() + "\" since it was last done.", exception);
		}
	}
}
