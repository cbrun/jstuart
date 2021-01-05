package fr.obeo.tools.stuart.mattermost.bot.tasks.google;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;

/**
 * Utilities to use the Google API.
 * 
 * @author flatombe
 *
 */
public class GoogleUtils {

	/**
	 * The name of this application, it will appear in the header of the messages
	 * sent to the Google API.
	 */
	private static final String APPLICATION_NAME = "JStuart Mattermost Bot";

	/**
	 * The location relative to the project root, of the google service account
	 * secret.
	 */
	private static final String SERVICE_ACCOUNT_SECRET_RESOURCE_NAME = "/google-service-account-secret.json";

	/**
	 * The scopes we need access to.
	 */
	private static final List<String> REQUESTED_SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS);

	/**
	 * Loads the secret credentials of our Google service account.
	 * 
	 * @return the {@link ServiceAccountCredentials}.
	 * @throws IOException
	 */
	private static ServiceAccountCredentials getServiceAccountSecret() throws IOException {
		return ServiceAccountCredentials
				.fromStream(GoogleUtils.class.getResourceAsStream(SERVICE_ACCOUNT_SECRET_RESOURCE_NAME));
	}

	/**
	 * Creates the credentials for accessing and manipulating the Google
	 * spreadsheets.
	 * 
	 * @return the created {@link GoogleCredentials} scoped with
	 *         {@link #REQUESTED_SCOPES}.
	 * @throws IOException
	 * @throws GeneralSecurityException
	 */
	private static GoogleCredentials getGoogleSheetsCredentials() throws IOException, GeneralSecurityException {
		return getServiceAccountSecret().createScoped(REQUESTED_SCOPES);
	}

	/**
	 * Provides the {@link Sheets Google Sheet} service.
	 * 
	 * @return the {@link Sheets} service.
	 * @throws IOException
	 * @throws GeneralSecurityException
	 */
	public static Sheets getSheetsService() throws IOException, GeneralSecurityException {
		return new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(),
				new HttpCredentialsAdapter(getGoogleSheetsCredentials())).setApplicationName(APPLICATION_NAME).build();
	}
}
