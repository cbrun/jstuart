package fr.obeo.tools.stuart.mattermost.bot.tasks;

import java.io.IOException;
import java.util.Objects;

import fr.obeo.tools.stuart.mattermost.bot.MMBot;
import fr.obeo.tools.stuart.mattermost.bot.MPost;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.CommandExecutionContext;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.CommandExecutionException;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.SharedTasksCommand;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.SharedTasksCommandFactory;

/**
 * Main entry point of the functionality to create and perform so-called "tasks"
 * through chat interactions in Mattermost channels monitored by the bot.
 * 
 * @author flatombe
 *
 */
public class SharedTasks {

	/**
	 * The ID of the Google spreadsheet to use. It can be found in the URL of the
	 * sheet, e.g.:
	 * "https://docs.google.com/spreadsheets/d/XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX/edit#gid=0"
	 */
	private final String sharedTasksSheetId;

	/**
	 * Creates a new {@link SharedTasks}.
	 * 
	 * @param sharedTasksSheetId the (non-{@code null}) ID of the Google spreadsheet
	 *                           to use.
	 */
	public SharedTasks(String sharedTasksSheetId) {
		this.sharedTasksSheetId = Objects.requireNonNull(sharedTasksSheetId);
	}

	/**
	 * Handles a message posted on a channel we are monitoring.
	 * 
	 * @param bot  our {@link MMBot}.
	 * @param post the {@link MPost} that may be a task-related command.
	 * @throws IOException
	 */
	public void handle(MMBot bot, MPost post) throws IOException {
		SharedTasksCommand command = new SharedTasksCommandFactory(bot).tryToParsePostIntoCommand(post);
		if (command != null) {
			try {
				command.execute(new CommandExecutionContext(bot, post, this.sharedTasksSheetId));
			} catch (CommandExecutionException exception) {
				throw new IOException("There was an issue while executing command \"" + command.toText() + "\".",
						exception);
			}
		}
		// Otherwise the post was not a valid command message so we just ignore it.
	}
}
