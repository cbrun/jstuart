package fr.obeo.tools.stuart.mattermost.bot.tasks.commands;

import java.io.IOException;

/**
 * {@link SharedTasksCommand} implementation for when we could not parse text
 * into a valid command to execute.
 * 
 * @author flatombe
 *
 */
public class ErrorCommand extends SharedTasksCommand {

	private final String errorMessage;

	public ErrorCommand(String text, String errorMessage) {
		super(text);
		this.errorMessage = errorMessage;
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
