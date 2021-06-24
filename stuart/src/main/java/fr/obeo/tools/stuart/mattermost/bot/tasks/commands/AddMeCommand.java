package fr.obeo.tools.stuart.mattermost.bot.tasks.commands;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;

import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.CommandDocumentation;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.CommandExecutionContext;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.CommandExecutionException;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.CommandWithTaskNameAndUserId;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.SharedTasksCommand;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.SharedTasksCommandFactory;
import fr.obeo.tools.stuart.mattermost.bot.tasks.google.GoogleUtils;
import fr.obeo.tools.stuart.mattermost.bot.tasks.google.SharedTasksGoogleUtils;

/**
 * {@link SharedTasksCommand} implementation for a user to add themselves to a
 * task in a channel.
 * 
 * @author flatombe
 *
 */
public class AddMeCommand extends CommandWithTaskNameAndUserId {
	public static CommandDocumentation DOCUMENTATION = new CommandDocumentation() {

		@Override
		public String getPurpose() {
			return "Registers the caller for the task";
		};

		@Override
		public String getUsage(String... commandArguments) {
			final String taskName = (commandArguments != null && commandArguments.length > 0) ? commandArguments[0]
					: "<task name>";
			return SharedTasksCommandFactory.COMMAND_STARTER + SharedTasksCommandFactory.VERB_ADDME
					+ SharedTasksCommandFactory.COMMAND_SEPARATOR + taskName;
		};
	};

	/**
	 * Creates a new {@link AddMeCommand}.
	 * 
	 * @param commandText         the (non-{@code null}) textual form of the
	 *                            command.
	 * @param mattermostChannelId the (non-{@code null}) ID of the Mattermost
	 *                            channel concerned by this command.
	 * @param taskName            the (non-{@code null}) name of the task the user
	 *                            wants to add themselves to.
	 * @param mattermostUserId    the (non-{@code null}) ID of the Mattermost user
	 *                            that wants to add themselves to the task.
	 */
	public AddMeCommand(String commandText, String mattermostChannelId, String taskName, String mattermostUserId) {
		super(commandText, mattermostChannelId, taskName, mattermostUserId);
	}

	@Override
	public void execute(CommandExecutionContext commandExecutionContext) throws CommandExecutionException {
		List<String> registeredUserIds = this.getAllRegisteredUserIds(commandExecutionContext);
		if (registeredUserIds != null) {
			if (registeredUserIds.contains(this.getUserId())) {
				userIsAlreadyRegistered(commandExecutionContext);
			} else {
				registerUser(commandExecutionContext);
			}
		}
		// Else we already responded with a message that the task does not exist.
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
				+ "\". To unregister yourself, use command ```" + SharedTasksCommandFactory.ALL_VERBS_DOCUMENTATION
						.get(SharedTasksCommandFactory.VERB_REMOVEME).getUsage(this.getTaskName())
				+ "```.";
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
					.append(commandExecutionContext.getSharedTasksSheetId(), range, body)
					.setValueInputOption(GoogleUtils.VALUE_INPUT_OPTION_RAW).execute();
			String successMessage = "Successfully registered "
					+ this.getMattermostUser(commandExecutionContext).getUsername() + " for task \""
					+ this.getTaskName() + "\".";
			commandExecutionContext.getBot().respond(commandExecutionContext.getPost(), successMessage);
		} catch (IOException | GeneralSecurityException exception) {
			throw new CommandExecutionException("There was an issue while registering user \"" + this.getUserId()
					+ "\" to task \"" + this.getTaskName() + "\".", exception);
		}
	}

}
