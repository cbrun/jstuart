package fr.obeo.tools.stuart.mattermost.bot.tasks.commands;

import java.io.IOException;

import com.google.api.services.sheets.v4.model.Sheet;

import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.CommandDocumentation;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.CommandExecutionContext;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.CommandExecutionException;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.CommandWithTaskName;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.SharedTasksCommand;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.SharedTasksCommandFactory;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.SharedTasksCommandFactory.SharedTasksVerb;
import fr.obeo.tools.stuart.mattermost.bot.tasks.google.GoogleException;
import fr.obeo.tools.stuart.mattermost.bot.tasks.google.SharedTasksGoogleUtils;

/**
 * {@link SharedTasksCommand} implementation for creating a new task in a
 * channel.
 * 
 * @author flatombe
 *
 */
public class CreateTaskCommand extends CommandWithTaskName {
	public static CommandDocumentation DOCUMENTATION = new CommandDocumentation() {

		@Override
		public String getPurpose() {
			return "Creates a new task for this channel";
		};

		@Override
		public String getUsage(String... commandArguments) {
			final String taskName = (commandArguments != null && commandArguments.length > 0) ? commandArguments[0]
					: "<task name>";
			return SharedTasksCommandFactory.COMMAND_STARTER + SharedTasksVerb.CREATE.getLabel()
					+ SharedTasksCommandFactory.COMMAND_SEPARATOR + taskName;
		};
	};

	/**
	 * Creates a new {@link CreatedTaskCommand}.
	 * 
	 * @param commandText         the (non-{@code null}) textual form of the
	 *                            command.
	 * @param mattermostChannelId the (non-{@code null}) ID of the Mattermost
	 *                            channel concerned by this command.
	 * @param taskName            the (non-{@code null}) name of the task to create.
	 */
	public CreateTaskCommand(String commandText, String mattermostChannelId, String taskName) {
		super(commandText, mattermostChannelId, taskName);
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
					+ "\". To register yourself for this task, use command ```"
					+ SharedTasksVerb.ADDME.getDocumentation().getUsage(this.getTaskName()) + "```.";
			context.getBot().respond(context.getPost(), successMessage);
		} catch (IOException | GoogleException exception) {
			throw new CommandExecutionException(
					"There was an issue while creating a new sheet for task \"" + this.getTaskName() + "\" in channel "
							+ this.getChannelId() + " in spreadsheet " + context.getSharedTasksSheetId() + ".",
					exception);
		}
	}
}
