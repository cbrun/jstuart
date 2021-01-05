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

	// TODO: temporary during development
	private static final String TEST_BOT_CHANNEL_ID = "whxbgbtqrtr5jetdri7o9bxqiw";
	private static final String DEVELOPER_USER_ID = "99i5q1xcktyj7nupf5tf3d4q4a";

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
				// TODO: temporary during development
				if (p.getChannelId().equals(TEST_BOT_CHANNEL_ID) && p.getUserId().equals(DEVELOPER_USER_ID)) {
					this.sharedTasks.handle(bot, p);
				}
			}
		}
	}
}
