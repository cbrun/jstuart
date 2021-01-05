package fr.obeo.tools.stuart.mattermost.bot.tasks.commands;

import fr.obeo.tools.stuart.mattermost.bot.MMBot;
import fr.obeo.tools.stuart.mattermost.bot.MPost;

/**
 * Wrapper for all the contextual elements required for executing a
 * {@link SharedTasksCommand}.
 * 
 * @author flatombe
 *
 */
public class CommandExecutionContext {

	private final MMBot bot;
	private final MPost post;
	private final String sharedTasksSheetId;

	/**
	 * Creates a new {@link CommandExecutionContext}.
	 * 
	 * @param bot                the (non-{@code null}) {@link MMBot} that executes
	 *                           the command.
	 * @param post               the (non-{@code null}) originating {@link MPost}
	 *                           that triggered the command execution.
	 * @param sharedTasksSheetId the (non-{@code null}) ID of the shared tasks
	 *                           Google spreadsheet to use.
	 */
	public CommandExecutionContext(MMBot bot, MPost post, String sharedTasksSheetId) {
		this.bot = bot;
		this.post = post;
		this.sharedTasksSheetId = sharedTasksSheetId;
	}

	/**
	 * Provides the bot that executes the command.
	 * 
	 * @return a (non-{@code null}) {@link MMBot}.
	 */
	public MMBot getBot() {
		return this.bot;
	}

	/**
	 * Provides the Mattermost post that triggered the execution of the command.
	 * 
	 * @return a (non-{@code null}) {@link MPost}.
	 */
	public MPost getPost() {
		return this.post;
	}

	/**
	 * Provides the ID of the Google spreadsheet used for the shared tasks.
	 * 
	 * @return a (non-{@code null}) {@link String}.
	 */
	public String getSharedTasksSheetId() {
		return this.sharedTasksSheetId;
	}

}
