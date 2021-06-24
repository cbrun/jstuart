package fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common;

/**
 * Captures the documentation elements of a {@link SharedTasksCommand}
 * implementation.
 * 
 * @author flatombe
 *
 */
public interface CommandDocumentation {

	/**
	 * Provides the purpose of the command.
	 * 
	 * @return the (non-{@code null}) purpose of the command. It should be rather
	 *         short.
	 */
	String getPurpose();

	/**
	 * Provides the syntax usage of the command.
	 * 
	 * @param commandArguments the (non-{@code null}) arguments that may be passed
	 *                         to the command. It is expected that there are as many
	 *                         elements in this array as the elements expected by
	 *                         the command itself.
	 * @return the (non-{@code null}) {@link String} showing how to use the command.
	 */
	String getUsage(String... commandArguments);
}