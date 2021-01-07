package fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common;

import java.util.Objects;

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
	 * @param mattermostUserId              the (non-{@code null}) ID of the user concerned by
	 *                            this command.
	 */
	public CommandWithTaskNameAndChannelIdAndUserId(String commandText, String taskName, String mattermostChannelId,
			String mattermostUserId) {
		super(commandText, taskName, mattermostChannelId);
		this.userId = Objects.requireNonNull(mattermostUserId);
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
