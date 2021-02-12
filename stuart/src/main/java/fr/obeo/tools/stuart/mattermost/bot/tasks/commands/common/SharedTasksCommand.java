package fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import fr.obeo.tools.stuart.mattermost.bot.user.MUser;

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
	 * Retrieves the Mattermost users matching the given user IDs. The user IDs must
	 * be existing user IDs, otherwise an {@link CommandExecutionException} is
	 * thrown, so this cannot be used to check whether any user with a particular
	 * user ID exists.
	 * 
	 * @param userIds the (non-{@code null}) {@link List} of user IDs for which we
	 *                are looking for the corresponding {@link MUser}.
	 * @return the (non-{@code null}) {@link Map} of {@link MUser} indexed by their
	 *         {@link MUser#getId() user ID}.
	 * @throws CommandExecutionException if there was an issue while executing the
	 *                                   command.
	 */
	protected Map<String, MUser> getUsersById(CommandExecutionContext commandExecutionContext, List<String> userIds)
			throws CommandExecutionException {
		try {
			Map<String, MUser> usersIndexedById = commandExecutionContext.getBot().getUsersById(userIds);
			if (usersIndexedById.size() != userIds.size()) {
				// Sanity check.
				throw new CommandExecutionException("There was an issue while retrieving users with IDs "
						+ userIds.stream().map(userId -> "\"" + userId + "\"").collect(Collectors.joining(", ")) + ".",
						new IllegalStateException(
								"Expected response to consist of as many users as the number of input user IDs ("
										+ userIds.size() + ") but there were " + usersIndexedById.size() + "."));
			} else if (!usersIndexedById.keySet().containsAll(userIds)) {
				// Sanity check.
				throw new CommandExecutionException("There was an issue while retrieving user with IDs "
						+ userIds.stream().map(userId -> "\"" + userId + "\"").collect(Collectors.joining(", ")) + ".",
						new IllegalStateException("Expected response to consist of one user per input user ID."));
			} else {
				return usersIndexedById;
			}
		} catch (IOException exception) {
			throw new CommandExecutionException("There was an issue while retrieving user with IDs "
					+ userIds.stream().map(userId -> "\"" + userId + "\"").collect(Collectors.joining(", ")) + ".",
					exception);
		}
	}

	/**
	 * Provides the textual form of this command.
	 * 
	 * @return the (non-{@code null}) {@link String} corresponding to this command.
	 */
	public String toText() {
		return this.text;
	}
}
