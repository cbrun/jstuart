package fr.obeo.tools.stuart.mattermost.bot.tasks.commands;

import java.io.IOException;
import java.util.List;

import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.CommandExecutionContext;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.CommandExecutionException;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.CommandWithTaskNameAndChannelIdAndUserId;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.SharedTasksCommand;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.SharedTasksCommandFactory;
import fr.obeo.tools.stuart.mattermost.bot.tasks.google.GoogleException;
import fr.obeo.tools.stuart.mattermost.bot.tasks.google.SharedTasksGoogleUtils;

/**
 * {@link SharedTasksCommand} implementation for a user to remove themselves
 * from a task in a channel.
 * 
 * @author flatombe
 *
 */
public class RemoveMeCommand extends CommandWithTaskNameAndChannelIdAndUserId {

	/**
	 * Creates a new {@link RemoveMeCommand}.
	 * 
	 * @param commandText         the (non-{@code null}) textual form of the
	 *                            command.
	 * @param taskName            the (non-{@code null}) name of the task the user
	 *                            wants to remove themselves from.
	 * @param mattermostChannelId the (non-{@code null}) ID of the Mattermost
	 *                            channel concerned by this command.
	 * @param mattermostUserId    the (non-{@code null}) ID of the Mattermost user
	 *                            that wants to remove themselves from the task.
	 */
	public RemoveMeCommand(String commandText, String taskName, String mattermostChannelId, String mattermostUserId) {
		super(commandText, taskName, mattermostChannelId, mattermostUserId);
	}

	@Override
	public void execute(CommandExecutionContext commandExecutionContext) throws CommandExecutionException {
		List<String> registeredUserIds = this.getAllRegisteredUserIds(commandExecutionContext);
		if (registeredUserIds != null) {
			if (!registeredUserIds.contains(this.getUserId())) {
				userIsNotRegistered(commandExecutionContext);
			} else {
				unregisterUser(commandExecutionContext);
			}
		}
		// Else we already responded with a message that the task does not exist.
	}

	/**
	 * Behavior to execute when the user who wants to unregister themselves from the
	 * task is actually not registered.
	 * 
	 * @param commandExecutionContext the (non-{@code null})
	 *                                {@link CommandExecutionContext}.
	 * @throws CommandExecutionException
	 */
	private void userIsNotRegistered(CommandExecutionContext commandExecutionContext) throws CommandExecutionException {
		String message = "Failed user unregistration from task \"" + this.getTaskName()
				+ "\" because you were not registered for this task in the first place. To register yourself, use command \""
				+ SharedTasksCommandFactory.ALL_VERBS_USAGE.get(SharedTasksCommandFactory.VERB_ADDME)
						.apply(this.getTaskName())
				+ "\".";
		try {
			commandExecutionContext.getBot().respond(commandExecutionContext.getPost(), message);
		} catch (IOException exception) {
			throw new CommandExecutionException(
					"There was an issue while responding to a user who is not registered to a task.", exception);
		}
	}

	/**
	 * Behavior to execute when the user who wants to unregister themselves from the
	 * task is already registered.
	 * 
	 * @param commandExecutionContext the (non-{@code null})
	 *                                {@link CommandExecutionContext}.
	 * @throws CommandExecutionException
	 */
	private void unregisterUser(CommandExecutionContext commandExecutionContext) throws CommandExecutionException {
		try {
			SharedTasksGoogleUtils.removeRegisteredUser(commandExecutionContext.getSharedTasksSheetId(),
					this.getTaskName(), this.getChannelId(), this.getUserId());

			String successMessage = "Successfully unregistered "
					+ this.getMattermostUser(commandExecutionContext).getUsername() + " from task \""
					+ this.getTaskName() + "\".";
			commandExecutionContext.getBot().respond(commandExecutionContext.getPost(), successMessage);
		} catch (GoogleException | IOException exception) {
			throw new CommandExecutionException("There was an issue while unregistering user \"" + this.getUserId()
					+ "\" to task \"" + this.getTaskName() + "\".", exception);
		}
	}

}
