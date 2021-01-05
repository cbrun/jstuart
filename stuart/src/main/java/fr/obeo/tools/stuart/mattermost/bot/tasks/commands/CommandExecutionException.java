package fr.obeo.tools.stuart.mattermost.bot.tasks.commands;

/**
 * {@link Exception} implementation for when something when wrong during the
 * {@link SharedTasksCommand#execute(CommandExecutionContext) execution} of a
 * {@link SharedTasksCommand}.
 * 
 * @author flatombe
 *
 */
public class CommandExecutionException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new {@link CommandExecutionException} with an additional message.
	 * 
	 * @param message the (maybe-{@code null}) exception message.
	 * @param cause   the (non-{@code null}) cause {@link Throwable}.
	 */
	public CommandExecutionException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Creates a new {@link CommandExecutionException}.
	 * 
	 * @param cause the (non-{@code null}) cause {@link Throwable}.
	 */
	public CommandExecutionException(Throwable cause) {
		this(null, cause);
	}

}
