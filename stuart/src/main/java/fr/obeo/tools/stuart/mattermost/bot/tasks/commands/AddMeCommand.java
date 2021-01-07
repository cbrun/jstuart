package fr.obeo.tools.stuart.mattermost.bot.tasks.commands;

import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.CommandExecutionContext;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.CommandExecutionException;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.CommandWithTaskNameAndChannelIdAndUserId;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.SharedTasksCommand;

/**
 * {@link SharedTasksCommand} implementation for a user to add themselves to a
 * task in a channel.
 * 
 * @author flatombe
 *
 */
public class AddMeCommand extends CommandWithTaskNameAndChannelIdAndUserId {

	/**
	 * Creates a new {@link AddMeCommand}.
	 * 
	 * @param commandText the (non-{@code null}) textual form of the command.
	 * @param taskName    the (non-{@code null}) name of the task the user wants to
	 *                    add themselves to.
	 * @param mattermostChannelId   the (non-{@code null}) ID of the Mattermost channel
	 *                    concerned by this command.
	 * @param mattermostUserId      the (non-{@code null}) ID of the Mattermost user that
	 *                    wants to add themselves to the task.
	 */
	public AddMeCommand(String commandText, String taskName, String mattermostChannelId, String mattermostUserId) {
		super(commandText, taskName, mattermostChannelId, mattermostUserId);
	}

	@Override
	public void execute(CommandExecutionContext commandExecutionContext) throws CommandExecutionException {
		// TODO: implement
		// 1. Find the sheet corresponding to the task + channel.
		// 2. Check whether the user is already registered in the task.
		// 3. Otherwise add them to the task.
	}

}
