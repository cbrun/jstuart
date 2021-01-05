package fr.obeo.tools.stuart.mattermost.bot.tasks.google;

import java.util.Objects;

/**
 * Wrapper for exceptions thrown by the Google API.
 * 
 * @author flatombe
 *
 */
public class GoogleException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public GoogleException(String message, Throwable cause) {
		super(Objects.requireNonNull(message), Objects.requireNonNull(cause));
	}

}
