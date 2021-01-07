package fr.obeo.tools.stuart.mattermost.bot.tasks.google;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

import com.google.api.services.sheets.v4.model.AddSheetRequest;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetResponse;
import com.google.api.services.sheets.v4.model.Request;
import com.google.api.services.sheets.v4.model.Sheet;
import com.google.api.services.sheets.v4.model.SheetProperties;
import com.google.api.services.sheets.v4.model.Spreadsheet;

import fr.obeo.tools.stuart.mattermost.bot.tasks.SharedTasks;

/**
 * Utilities related to how we use a Google spreadsheet as the persistence layer
 * for {@link SharedTasks}.
 * 
 * @author flatombe
 *
 */
public class SharedTasksGoogleUtils {

	/**
	 * Provides the {@link Sheet} from the given spreadsheet used for the given task
	 * in the given channel.
	 * 
	 * @param spreadsheetId       the (non-{@code null}) ID of the spreadsheet.
	 * @param taskName            the (non-{@code null}) name of the task.
	 * @param mattermostChannelId the (non-{@code null}) ID of the Mattermost
	 *                            channel.
	 * @return the {@link Sheet} for the task, or {@code null} if no such sheet
	 *         exists.
	 * @throws GoogleException
	 */
	public static Sheet getSheetForTask(String spreadsheetId, String taskName, String mattermostChannelId)
			throws GoogleException {
		Objects.requireNonNull(spreadsheetId);
		Objects.requireNonNull(taskName);
		Objects.requireNonNull(mattermostChannelId);

		String sheetTitleForTask = getSheetTitleForTask(taskName, mattermostChannelId);
		try {
			Spreadsheet spreadsheetDocument = GoogleUtils.getSheetsService().spreadsheets().get(spreadsheetId)
					.execute();
			Optional<Sheet> sheetForTask = spreadsheetDocument.getSheets().stream()
					.filter(candidate -> candidate.getProperties().getTitle().equalsIgnoreCase(sheetTitleForTask))
					.reduce((left, right) -> {
						throw new IllegalStateException("Found two sheets with the title \"" + sheetTitleForTask
								+ "\" in spreadsheet " + spreadsheetId);
					});
			return sheetForTask.orElse(null);
		} catch (IOException | GeneralSecurityException exception) {
			throw new GoogleException("There was an issue while trying to load spreadsheet \"" + spreadsheetId + "\".",
					exception);
		}
	}

	/**
	 * Provides the title for the sheet in the spreadsheet that will hold the
	 * information regarding a particular task of a channel.
	 * 
	 * @param taskName            the (non-{@code null}) name of the task.
	 * @param mattermostChannelId the (non-{@code null}) ID of the Mattermost
	 *                            channel.
	 * @return the (non-{@code null}) title for the sheet corresponding to the task.
	 */
	public static String getSheetTitleForTask(String taskName, String mattermostChannelId) {
		Objects.requireNonNull(taskName);
		Objects.requireNonNull(mattermostChannelId);

		return taskName + "_" + mattermostChannelId;
	}

	/**
	 * Creates a new {@link Sheet} in a spreadsheet.
	 * 
	 * @param spreadsheetId the (non-{@code null}) ID of the spreadsheet in which to
	 *                      create the {@link Sheet}.
	 * @param sheetTitle    the (non-{@code null}) desired title for the created
	 *                      {@link Sheet}.
	 * @return the (non-{@code null}) ID of the created {@link Sheet}.
	 * @throws GoogleException
	 */
	public static Integer createNewSheet(String spreadsheetId, String sheetTitle) throws GoogleException {
		Objects.requireNonNull(spreadsheetId);
		Objects.requireNonNull(sheetTitle);

		AddSheetRequest addSheetRequest = new AddSheetRequest()
				.setProperties(new SheetProperties().setTitle(sheetTitle));

		Request createSheetRequest = new Request().setAddSheet(addSheetRequest);
		BatchUpdateSpreadsheetRequest batchUpdateRequest = new BatchUpdateSpreadsheetRequest()
				.setRequests(Collections.singletonList(createSheetRequest));

		try {
			BatchUpdateSpreadsheetResponse response = GoogleUtils.getSheetsService().spreadsheets()
					.batchUpdate(spreadsheetId, batchUpdateRequest).execute();
			return response.getReplies().get(batchUpdateRequest.getRequests().indexOf(createSheetRequest)).getAddSheet()
					.getProperties().getSheetId();
		} catch (IOException | GeneralSecurityException exception) {
			throw new GoogleException("There was an unexpected issue while creating a new sheet", exception);
		}
	}
}
