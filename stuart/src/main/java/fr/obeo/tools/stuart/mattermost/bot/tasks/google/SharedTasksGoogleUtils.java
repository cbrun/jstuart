package fr.obeo.tools.stuart.mattermost.bot.tasks.google;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.google.api.services.sheets.v4.model.AddSheetRequest;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetResponse;
import com.google.api.services.sheets.v4.model.Request;
import com.google.api.services.sheets.v4.model.Sheet;
import com.google.api.services.sheets.v4.model.SheetProperties;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;

import fr.obeo.tools.stuart.mattermost.bot.tasks.SharedTasks;

/**
 * Utilities related to how we use a Google spreadsheet as the persistence layer
 * for {@link SharedTasks}.
 * 
 * @author flatombe
 *
 */
public class SharedTasksGoogleUtils {

	public static final String VALUE_INPUT_OPTION_RAW = "RAW";
	public static final String USER_ID__COLUMN = "A";
	public static final String LAST_TIME_STAMP__COLUMN = "B";
	public static final String AFFECTED_USER_ID__COLUMN = "C";
	public static final String RECORD_USER_ID__COLUMN = "D";
	public static final String RECORD_TIME_STAMP__COLUMN = "E";

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

	/**
	 * Provides all the IDs of the Mattermost users registered for a task.
	 * 
	 * @param spreadsheetId       the (non-{@code null}) ID of the spreadsheet.
	 * @param taskName            the (non-{@code null}) name of the task.
	 * @param mattermostChannelId the (non-{@code null}) ID of the Mattermost
	 *                            channel.
	 * @return the (non-{@code null}) {@link List} of all the IDs of the Mattermost
	 *         users registered for the task.
	 * @throws GoogleException
	 */
	public static List<String> getAllRegisteredUserIds(String spreadsheetId, String taskName,
			String mattermostChannelId) throws GoogleException {
		Objects.requireNonNull(spreadsheetId);
		Objects.requireNonNull(taskName);
		Objects.requireNonNull(mattermostChannelId);

		String range = getAllUsersRangeForTask(taskName, mattermostChannelId);
		try {
			ValueRange result = GoogleUtils.getSheetsService().spreadsheets().values().get(spreadsheetId, range)
					.execute();
			if (result.getValues() != null) {
				// If the cast fail it means that was an issue while inserting a user ID in the
				// first column, as we should only handle strings.
				return result.getValues().stream().map(value -> (String) value.get(0)).collect(Collectors.toList());
			} else {
				return new ArrayList<>();
			}
		} catch (IOException | GeneralSecurityException exception) {
			throw new GoogleException("There was an issue while retrieving range \"" + range + "\" in spreadsheet \""
					+ spreadsheetId + "\".", exception);
		}
	}

	/**
	 * Provides the Google spreadsheet "range" that contains all users registered
	 * for a task.
	 * 
	 * @param taskName            the (non-{@code null}) name of the task.
	 * @param mattermostChannelId the (non-{@code null}) ID of the Mattermost
	 *                            channel.
	 * @return the range, e.g. "SheetName!X:Y" that holds all the users registered
	 *         for the task.
	 */
	public static String getAllUsersRangeForTask(String taskName, String mattermostChannelId) {
		// User IDs are stored in the first column of the sheet.
		return getSheetTitleForTask(taskName, mattermostChannelId) + "!" + USER_ID__COLUMN + ":" + USER_ID__COLUMN;
	}

	/**
	 * Provides the Google spreadsheet "range" that contains all the affected users
	 * to the "task".
	 * 
	 * @param taskName            the (non-{@code null}) name of the task.
	 * @param mattermostChannelId the (non-{@code null}) ID of the Mattermost
	 *                            channel.
	 * @return the range, e.g. "SheetName!X:Y" that holds all the affected users.
	 */
	public static String getAffectedUsersRange(String taskName, String mattermostChannelId) {
		Objects.requireNonNull(taskName);
		Objects.requireNonNull(mattermostChannelId);

		return getSheetTitleForTask(taskName, mattermostChannelId) + "!" + AFFECTED_USER_ID__COLUMN + ":"
				+ AFFECTED_USER_ID__COLUMN;
	}

	/**
	 * Provides the Google spreadsheet "range" that contains all the task
	 * realizations that is all the users with the associated task realization
	 * timeStamp.
	 * 
	 * @param taskName            the (non-{@code null}) name of the task.
	 * @param mattermostChannelId the (non-{@code null}) ID of the Mattermost
	 *                            channel.
	 * @return the range, e.g. "SheetName!X:Y" that holds all the tasks realization
	 *         records.
	 */
	public static String getTaskRealizationRecordsRange(String taskName, String mattermostChannelId) {
		Objects.requireNonNull(taskName);
		Objects.requireNonNull(mattermostChannelId);

		return getSheetTitleForTask(taskName, mattermostChannelId) + "!" + RECORD_USER_ID__COLUMN + ":"
				+ RECORD_TIME_STAMP__COLUMN;
	}

	/**
	 * 
	 * @param spreadsheetId       the (non-{@code null}) ID of the spreadsheet
	 *                            document.
	 * @param taskName            the (non-{@code null}) name of the task.
	 * @param mattermostChannelId the (non-{@code null}) ID of the Mattermost
	 *                            channel.
	 * @param userId              the (non-{@code null}) ID of the Mattermost user
	 *                            to unregister from the task.
	 * @throws GoogleException
	 */
	public static void removeRegisteredUser(String spreadsheetId, String taskName, String mattermostChannelId,
			String userId) throws GoogleException {
		String range = getAllUsersRangeForTask(taskName, mattermostChannelId);
		try {
			ValueRange registeredUsersValueRange = GoogleUtils.getSheetsService().spreadsheets().values()
					.get(spreadsheetId, range).execute();
			List<List<Object>> currentValues = registeredUsersValueRange.getValues();
			if (currentValues != null) {
				// We remove the row corresponding to the user who is unregistering.
				List<List<Object>> newValues = currentValues.stream()
						.filter(row -> !row.isEmpty() && !row.get(0).equals(userId)).collect(Collectors.toList());
				// Add an empty row at the end so that the row that previously held the last
				// value is blanked.
				newValues.add(Collections.singletonList(""));

				// Sanitary check.
				if (newValues.size() != currentValues.size()) {
					throw new IllegalStateException("Removing user \"" + userId + "\" from task \"" + taskName
							+ "\" should have removed exactly one line, but it looks like it actually removed "
							+ (newValues.size() - 1 - currentValues.size()) + " lines.");
				}
				ValueRange body = new ValueRange().setValues(newValues);
				UpdateValuesResponse updateResult = GoogleUtils.getSheetsService().spreadsheets().values()
						.update(spreadsheetId, range, body).setValueInputOption(VALUE_INPUT_OPTION_RAW).execute();
			} else {
				throw new IllegalStateException("Cannot remove user \"" + userId + "\" from task \"" + taskName
						+ "\" because they are not registered for this task.");
			}
		} catch (IOException | GeneralSecurityException exception) {
			throw new GoogleException("There was an issue while retrieving range \"" + range + "\" in spreadsheet \""
					+ spreadsheetId + "\".", exception);
		}
	}

	/**
	 * Set a task as done.</br>
	 * - It will add a new record in the history.</br>
	 * - It will also reset the affected users to this task.
	 * 
	 * @param spreadsheetId       the (non-{@code null}) ID of the spreadsheet
	 *                            document.
	 * @param taskName            the (non-{@code null}) name of the task.
	 * @param mattermostChannelId the (non-{@code null}) ID of the Mattermost
	 *                            channel.
	 * @param userId              the (non-{@code null}) ID of the Mattermost user
	 *                            to unregister from the task.
	 * @throws GoogleException
	 */
	public static void doTask(String spreadsheetId, String taskName, String mattermostChannelId, String userId)
			throws GoogleException {
		Objects.requireNonNull(spreadsheetId);
		Objects.requireNonNull(taskName);
		Objects.requireNonNull(mattermostChannelId);
		Objects.requireNonNull(userId);

		// Update the records
		String range = getTaskRealizationRecordsRange(taskName, mattermostChannelId);
		try {
			ValueRange registeredUsersValueRange = GoogleUtils.getSheetsService().spreadsheets().values()
					.get(spreadsheetId, range).execute();
			List<List<Object>> currentValues = registeredUsersValueRange.getValues();
			if (currentValues == null) {
				currentValues = new ArrayList<>();
			}
			// Add a record in the list of rows
			currentValues.add(Arrays.asList(userId, Instant.now().toString()));
			ValueRange body = new ValueRange().setValues(currentValues);
			GoogleUtils.getSheetsService().spreadsheets().values().update(spreadsheetId, range, body)
					.setValueInputOption(VALUE_INPUT_OPTION_RAW).execute();
		} catch (IOException | GeneralSecurityException exception) {
			throw new GoogleException("There was an issue while retrieving range \"" + range + "\" in spreadsheet \""
					+ spreadsheetId + "\".", exception);
		}

		// Reset the affected users
		range = getAffectedUsersRange(taskName, mattermostChannelId);
		try {
			List<List<Object>> emptyValues = new ArrayList<>();
			emptyValues.add(Arrays.asList(""));

			ValueRange body = new ValueRange().setValues(emptyValues);
			GoogleUtils.getSheetsService().spreadsheets().values().update(spreadsheetId, range, body)
					.setValueInputOption(VALUE_INPUT_OPTION_RAW).execute();
		} catch (IOException | GeneralSecurityException exception) {
			throw new GoogleException("There was an issue while resetting range \"" + range + "\" in spreadsheet \""
					+ spreadsheetId + "\".", exception);
		}
	}

}
