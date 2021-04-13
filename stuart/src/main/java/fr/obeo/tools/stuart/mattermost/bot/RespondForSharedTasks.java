package fr.obeo.tools.stuart.mattermost.bot;

import java.io.IOException;

import fr.obeo.tools.stuart.mattermost.bot.tasks.SharedTasks;

/**
 * {@link ReactOnMessage} implementation for the {@link SharedTasks} feature.
 * 
 * @author flatombe
 *
 */
public class RespondForSharedTasks implements ReactOnMessage {

	/**
	 * The ID of the Google Spreadsheet to use.
	 */
	private final String sharedTasksSheetId;

	/**
	 * The {@link SharedTasks} that will handle the message. May be {@code null}.
	 */
	private SharedTasks sharedTasks;

	/**
	 * Creates a new {@link RespondForSharedTasks}.
	 * 
	 * @param sharedTasksSheetId the (maybe-{@code null}) ID of the Google
	 *                           spreadsheet to use.
	 */
	public RespondForSharedTasks(String sharedTasksSheetId) {
		this.sharedTasksSheetId = sharedTasksSheetId;
		if (this.sharedTasksSheetId != null) {
			this.sharedTasks = new SharedTasks(this.sharedTasksSheetId);
		}
	}

	@Override
	public void onMessage(MMBot bot, MPost p) throws IOException {
		if (!p.isFromWebhook() && isNotFromBotOrIsDiagnostic(bot, p)) {
			if (this.sharedTasks != null) {
				this.sharedTasks.handle(bot, p);
			}
		}
	}
}
