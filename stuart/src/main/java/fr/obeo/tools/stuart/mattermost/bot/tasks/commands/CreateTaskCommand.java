package fr.obeo.tools.stuart.mattermost.bot.tasks.commands;

import java.io.IOException;

import com.google.api.services.sheets.v4.model.Sheet;

import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.CommandExecutionContext;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.CommandExecutionException;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.CommandWithTaskNameAndChannelId;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.SharedTasksCommand;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.SharedTasksCommandFactory;
import fr.obeo.tools.stuart.mattermost.bot.tasks.google.GoogleException;
import fr.obeo.tools.stuart.mattermost.bot.tasks.google.SharedTasksGoogleUtils;

/**
 * {@link SharedTasksCommand} implementation for creating a new task in a
 * channel.
 * 
 * @author flatombe
 *
 */
public class CreateTaskCommand extends CommandWithTaskNameAndChannelId {
	public static CommandWithTaskNameAndChannelId.CommandInformation INFORMATION = new CommandWithTaskNameAndChannelId.CommandInformation() {
		public String getDocumentation() {
			return "To create a new task for this channel";
		};

		public String getUsage(String taskName) {
			return SharedTasksCommandFactory.COMMAND_STARTER + SharedTasksCommandFactory.VERB_CREATE
					+ SharedTasksCommandFactory.COMMAND_SEPARATOR + (taskName != null ? taskName : "<task name>");
		};
	};

	/**
	 * Creates a new {@link CreatedTaskCommand}.
	 * 
	 * @param commandText         the (non-{@code null}) textual form of the
	 *                            command.
	 * @param taskName            the (non-{@code null}) name of the task to create.
	 * @param mattermostChannelId the (non-{@code null}) ID of the Mattermost
	 *                            channel concerned by this command.
	 */
	public CreateTaskCommand(String commandText, String taskName, String mattermostChannelId) {
		super(commandText, taskName, mattermostChannelId);
	}

	@Override
	public void execute(CommandExecutionContext context) throws CommandExecutionException {
		Sheet taskSheet = getTaskSheet(context);
		if (taskSheet != null) {
			taskAlreadyExists(context);
		} else {
			createSheetForTask(context);
		}
	}

	/**
	 * Behavior to execute when the task we wanted to create already exists.
	 * 
	 * @param context the (non-{@code null}) {@link CommandExecutionContext}.
	 * @throws CommandExecutionException
	 */
	private void taskAlreadyExists(CommandExecutionContext context) throws CommandExecutionException {
		try {
			context.getBot().respond(context.getPost(),
					"Failed task creation. Task \"" + this.getTaskName() + "\" already exists.");
		} catch (IOException exception) {
			throw new CommandExecutionException(
					"There was an issue while responding to the creation of a task that already exists.", exception);
		}
	}

	/**
	 * Behavior to execute to create a new sheet that will be used for the
	 * newly-created task.
	 * 
	 * @param context the (non-{@code null}) {@link CommandExecutionContext}.
	 * @throws CommandExecutionException
	 */
	private void createSheetForTask(CommandExecutionContext context) throws CommandExecutionException {
		try {
			SharedTasksGoogleUtils.createNewSheet(context.getSharedTasksSheetId(),
					SharedTasksGoogleUtils.getSheetTitleForTask(this.getTaskName(), this.getChannelId()));
			String successMessage = "Successfully created task \"" + this.getTaskName()
					+ "\". To register yourself for this task, use command \""
					+ SharedTasksCommandFactory.ALL_VERBS_INFORMATION.get(SharedTasksCommandFactory.VERB_ADDME)
							.getUsage(this.getTaskName())
					+ "\".";
			context.getBot().respond(context.getPost(), successMessage);
		} catch (IOException | GoogleException exception) {
			throw new CommandExecutionException(
					"There was an issue while creating a new sheet for task \"" + this.getTaskName() + "\" in channel "
							+ this.getChannelId() + " in spreadsheet " + context.getSharedTasksSheetId() + ".",
					exception);
		}
	}
}
