package fr.obeo.tools.stuart.mattermost.bot.tasks.commands;

import java.util.Objects;

public abstract class SharedTasksCommand {

	/**
	 * Parses the text entered by a user that corresponds to a command into a
	 * {@link SharedTasksCommand}.
	 * 
	 * @param commandText the (non-{@code null}) input text, stripped from the
	 *                    command started symbol(s).
	 * @return the corresponding {@link SharedTasksCommand}.
	 */
	public static SharedTasksCommand parseFrom(String commandText) {
		Objects.requireNonNull(commandText);

		// TODO: implement parsing text into commands as per the specification

		// Placeholder
		return new ErrorCommand(commandText,
				"Parser does not yet support parsing the following text into a command: " + commandText);
	}

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
