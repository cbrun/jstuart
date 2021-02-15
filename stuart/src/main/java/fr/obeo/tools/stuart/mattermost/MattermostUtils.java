package fr.obeo.tools.stuart.mattermost;

public class MattermostUtils {

	/**
	 * Located right before a username, this notifies the mentioned user.
	 */
	public static final String HIGHLIGHT = "@";

	/**
	 * In Mattermost, all channel IDs have 26 characters. (I could not find where it
	 * is specified in the documentation though).
	 */
	public static final int CHANNEL_ID_LENGTH = 26;

}
