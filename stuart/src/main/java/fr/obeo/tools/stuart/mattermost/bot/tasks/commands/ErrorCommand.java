package fr.obeo.tools.stuart.mattermost.bot.tasks.commands;

import java.io.IOException;
import java.util.Objects;

import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.CommandExecutionContext;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.CommandExecutionException;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.SharedTasksCommand;

/**
 * {@link SharedTasksCommand} implementation for when we could not parse text
 * into a valid command to execute. This is not really a command usable by the
 * user, but rather the representation of a failed command creation, providing
 * feedback to the user as to which part of the syntax was incorrect or missing.
 * 
 * @author flatombe
 *
 */
public class ErrorCommand extends SharedTasksCommand {

	private final String errorMessage;

	/**
	 * Creates a new {@link ErrorCommand}.
	 * 
	 * @param commandText         the (non-{@code null}) textual form of the
	 *                            command.
	 * @param mattermostChannelId the (non-{@code null}) ID of the Mattermost
	 *                            channel.
	 * @param errorMessage        the (non-{@code null}) additional error message.
	 */
	public ErrorCommand(String commandText, String mattermostChannelId, String errorMessage) {
		super(commandText, mattermostChannelId);
		this.errorMessage = Objects.requireNonNull(errorMessage);
	}

	@Override
	public void execute(CommandExecutionContext commandExecutionContext) throws CommandExecutionException {
		try {
			commandExecutionContext.getBot().respond(commandExecutionContext.getPost(), this.errorMessage);
		} catch (IOException ioException) {
			throw new CommandExecutionException(ioException);
		}
	}

}
