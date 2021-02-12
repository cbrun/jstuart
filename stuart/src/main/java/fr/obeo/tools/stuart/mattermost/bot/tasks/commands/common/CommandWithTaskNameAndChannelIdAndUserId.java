package fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common;

import java.util.Collections;
import java.util.Objects;

import fr.obeo.tools.stuart.mattermost.bot.user.MUser;

/**
 * Partial implementation of {@link CommandWithTaskNameAndChannelId} with a
 * Mattermost user ID as well.
 * 
 * @author flatombe
 *
 */
public abstract class CommandWithTaskNameAndChannelIdAndUserId extends CommandWithTaskNameAndChannelId {

	private final String userId;

	/**
	 * Creates a new {@link CommandWithTaskNameAndChannelIdAndUserId}.
	 * 
	 * @param commandText         the (non-{@code null}) textual form of the command
	 * @param taskName            the (non-{@code null}) name of the task concerned
	 *                            by this command.
	 * @param mattermostChannelId the (non-{@code null}) ID of the channel concerned
	 *                            by this command.
	 * @param mattermostUserId    the (non-{@code null}) ID of the user concerned by
	 *                            this command.
	 */
	public CommandWithTaskNameAndChannelIdAndUserId(String commandText, String taskName, String mattermostChannelId,
			String mattermostUserId) {
		super(commandText, taskName, mattermostChannelId);
		this.userId = Objects.requireNonNull(mattermostUserId);
	}

	/**
	 * Retrieves the Mattermost user corresponding to the contextual Mattermost user
	 * ID of this command.
	 * 
	 * @param commandExecutionContext the (non-{@code null})
	 *                                {@link CommandExecutionContext}.
	 * @return the {@link MUser} with the user ID of this command.
	 * @throws CommandExecutionException
	 */
	protected MUser getMattermostUser(CommandExecutionContext commandExecutionContext)
			throws CommandExecutionException {
		return this.getUsersById(commandExecutionContext, Collections.singletonList(this.getUserId()))
				.get(this.getUserId());
	}

	/**
	 * Provides the ID the Mattermost user concerned by this command.
	 * 
	 * @return the (non-{@code null}) ID of the Mattermost user.
	 */
	public String getUserId() {
		return this.userId;
	}

}
