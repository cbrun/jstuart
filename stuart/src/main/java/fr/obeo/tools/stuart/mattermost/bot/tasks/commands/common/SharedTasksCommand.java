package fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common;

public abstract class SharedTasksCommand {

	private final String text;

	/**
	 * Creates a new {@link SharedTasksCommand}.
	 * 
	 * @param commandText the (non-{@code null}) textual form of the command.
	 */
	public SharedTasksCommand(String commandText) {
		this.text = commandText;
	}

	/**
	 * Executes this command.
	 * 
	 * @param commandExecutionContext the (non-{@code null})
	 *                                {@link CommandExecutionContext context} in
	 *                                which this command is executed.
	 * @throws CommandExecutionException if there was an issue while executing the
	 *                                   command.
	 */
	public abstract void execute(CommandExecutionContext commandExecutionContext) throws CommandExecutionException;

	/**
	 * Provides the textual form of this command.
	 * 
	 * @return the (non-{@code null}) {@link String} corresponding to this command.
	 */
	public String toText() {
		return this.text;
	}
}
