package fr.obeo.tools.stuart.mattermost.bot.tasks.commands;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.Sheet;
import com.google.api.services.sheets.v4.model.ValueRange;

import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.CommandExecutionContext;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.CommandExecutionException;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.CommandWithTaskNameAndChannelIdAndUserId;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.SharedTasksCommand;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.SharedTasksCommandFactory;
import fr.obeo.tools.stuart.mattermost.bot.tasks.google.GoogleException;
import fr.obeo.tools.stuart.mattermost.bot.tasks.google.GoogleUtils;
import fr.obeo.tools.stuart.mattermost.bot.tasks.google.SharedTasksGoogleUtils;

/**
 * {@link SharedTasksCommand} implementation for a user to add themselves to a
 * task in a channel.
 * 
 * @author flatombe
 *
 */
public class AddMeCommand extends CommandWithTaskNameAndChannelIdAndUserId {

	/**
	 * Creates a new {@link AddMeCommand}.
	 * 
	 * @param commandText         the (non-{@code null}) textual form of the
	 *                            command.
	 * @param taskName            the (non-{@code null}) name of the task the user
	 *                            wants to add themselves to.
	 * @param mattermostChannelId the (non-{@code null}) ID of the Mattermost
	 *                            channel concerned by this command.
	 * @param mattermostUserId    the (non-{@code null}) ID of the Mattermost user
	 *                            that wants to add themselves to the task.
	 */
	public AddMeCommand(String commandText, String taskName, String mattermostChannelId, String mattermostUserId) {
		super(commandText, taskName, mattermostChannelId, mattermostUserId);
	}

	@Override
	public void execute(CommandExecutionContext commandExecutionContext) throws CommandExecutionException {
		Sheet taskSheet = this.getExistingTaskSheetElseRespondWithMessage(commandExecutionContext);
		if (taskSheet != null) {
			List<String> registeredUserIds;
			try {
				registeredUserIds = SharedTasksGoogleUtils.getAllRegisteredUserIds(
						commandExecutionContext.getSharedTasksSheetId(), this.getTaskName(), this.getChannelId());
				if (registeredUserIds.contains(this.getUserId())) {
					userIsAlreadyRegistered(commandExecutionContext);
				} else {
					registerUser(commandExecutionContext);
				}
			} catch (GoogleException exception) {
				throw new CommandExecutionException(
						"There was an issue while retrieving the users registered for task \"" + this.getTaskName()
								+ "\".",
						exception);
			}
		}
	}

	/**
	 * Behavior to execute when the user who wants to register themselves to the
	 * task is already registered.
	 * 
	 * @param commandExecutionContext the (non-{@code null})
	 *                                {@link CommandExecutionContext}.
	 * @throws CommandExecutionException
	 */
	private void userIsAlreadyRegistered(CommandExecutionContext commandExecutionContext)
			throws CommandExecutionException {
		String message = "Failed user registration. You are already registered for task \"" + this.getTaskName()
				+ "\". To unregister yourself, use command \"" + SharedTasksCommandFactory.COMMAND_STARTER
				+ this.getTaskName() + SharedTasksCommandFactory.COMMAND_SEPARATOR
				+ SharedTasksCommandFactory.VERB_REMOVEME + "\".";
		try {
			commandExecutionContext.getBot().respond(commandExecutionContext.getPost(), message);
		} catch (IOException exception) {
			throw new CommandExecutionException(
					"There was an issue while responding to a user who is already registered a task.", exception);
		}
	}

	/**
	 * Behavior to execute when the user who wants to register themselves to the
	 * task is not already registered.
	 * 
	 * @param commandExecutionContext the (non-{@code null})
	 *                                {@link CommandExecutionContext}.
	 * @throws CommandExecutionException
	 */
	private void registerUser(CommandExecutionContext commandExecutionContext) throws CommandExecutionException {
		List<List<Object>> values = Arrays.asList(Arrays.asList(this.getUserId()));
		ValueRange body = new ValueRange().setValues(values);
		String range = SharedTasksGoogleUtils.getAllUsersRangeForTask(this.getTaskName(), this.getChannelId());
		try {
			AppendValuesResponse result = GoogleUtils.getSheetsService().spreadsheets().values()
					.append(commandExecutionContext.getSharedTasksSheetId(), range, body).setValueInputOption("RAW")
					.execute();
			String successMessage = "Successfully registered user for task \"" + this.getTaskName() + "\".";
			commandExecutionContext.getBot().respond(commandExecutionContext.getPost(), successMessage);
		} catch (IOException | GeneralSecurityException exception) {
			throw new CommandExecutionException("There was an issue while registering user \"" + this.getUserId()
					+ "\" to task \"" + this.getTaskName() + "\".", exception);
		}
	}

}
