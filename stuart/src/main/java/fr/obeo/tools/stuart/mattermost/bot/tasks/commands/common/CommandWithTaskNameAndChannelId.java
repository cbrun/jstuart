package fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common;

/**
 * Partial {@link SharedTasksCommand} implementation that holds a task name and
 * a mattermost channel ID.
 * 
 * @author flatombe
 *
 */
public abstract class CommandWithTaskNameAndChannelId extends SharedTasksCommand {

	private final String taskName;

	private final String channelId;

	/**
	 * Creates a new {@link CommandWithTaskNameAndChannelId}.
	 * 
	 * @param commandText         the (non-{@code null}) textual form of the
	 *                            command.
	 * @param taskName            the (non-{@code null}) name of the task concerned
	 *                            by this command.
	 * @param mattermostChannelId the (non-{@code null}) ID of the channel concerned
	 *                            by this command.
	 */
	public CommandWithTaskNameAndChannelId(String commandText, String taskName, String mattermostChannelId) {
		super(commandText);
		this.taskName = taskName;
		this.channelId = mattermostChannelId;
	}

	/**
	 * Provides the name of the task concerned by this command.
	 * 
	 * @return the (non-{@code null}) task name as typed by the user when creating
	 *         this command.
	 */
	public String getTaskName() {
		return this.taskName;
	}

	/**
	 * Provides the ID of the Mattermost channel concerned by this command.
	 * 
	 * @return the (non-{@code null}) ID of the Mattermost channel.
	 */
	public String getChannelId() {
		return this.channelId;
	}
}
