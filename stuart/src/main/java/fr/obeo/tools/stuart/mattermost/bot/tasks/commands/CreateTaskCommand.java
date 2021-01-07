package fr.obeo.tools.stuart.mattermost.bot.tasks.commands;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import com.google.api.services.sheets.v4.model.AddSheetRequest;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest;
import com.google.api.services.sheets.v4.model.Request;
import com.google.api.services.sheets.v4.model.SheetProperties;
import com.google.api.services.sheets.v4.model.Spreadsheet;

import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.CommandExecutionContext;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.CommandExecutionException;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.CommandWithTaskNameAndChannelId;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.SharedTasksCommand;
import fr.obeo.tools.stuart.mattermost.bot.tasks.google.GoogleException;
import fr.obeo.tools.stuart.mattermost.bot.tasks.google.GoogleUtils;

/**
 * {@link SharedTasksCommand} implementation for creating a new task in a
 * channel.
 * 
 * @author flatombe
 *
 */
public class CreateTaskCommand extends CommandWithTaskNameAndChannelId {

	/**
	 * Creates a new {@link CreatedTaskCommand}.
	 * 
	 * @param commandText         the (non-{@code null}) textual form of the
	 *                            command.
	 * @param taskName            the (non-{@code null}) name of the task to create.
	 * @param mattermostChannelId the (non-{@code null}) ID of the Mattermost
	 *                            channel concerned by this command.
	 */
	public CreateTaskCommand(String commandText, String taskName, String mattermostChannelId) {
		super(commandText, taskName, mattermostChannelId);
	}

	@Override
	public void execute(CommandExecutionContext context) throws CommandExecutionException {
		// TODO: implement
		// 1. First check whether there already exists a task with the name for this
		// channel
		// 2. Create a new sheet for that task.
		// 3. Display confirmation message and invitation for people to add themselves
		// to the task.

		try {
			int previousNumberOfSheets = getNumberOfSheets(context.getSharedTasksSheetId());
			createNewSheet(context.getSharedTasksSheetId(), "new sheet");
			int newNumberOfSheets = getNumberOfSheets(context.getSharedTasksSheetId());
			context.getBot().respond(context.getPost(), previousNumberOfSheets + " -> " + newNumberOfSheets);
		} catch (GoogleException | IOException exception) {
			throw new CommandExecutionException(exception);
		}
	}

	// Example to retrieve the number of sheets in the spreadsheet.
	private static int getNumberOfSheets(String spreadsheetId) throws GoogleException {
		try {
			Spreadsheet spreadsheetDocument = GoogleUtils.getSheetsService().spreadsheets().get(spreadsheetId)
					.execute();
			return spreadsheetDocument.getSheets().size();
		} catch (IOException | GeneralSecurityException exception) {
			throw new GoogleException("There was an unexpected issue while retrieving the number of sheets", exception);
		}
	}

	// Example to create a new sheet in the spreadsheet.
	private static void createNewSheet(String spreadsheetId, String sheetName) throws GoogleException {
		// Create a new AddSheetRequest
		AddSheetRequest addSheetRequest = new AddSheetRequest();
		SheetProperties sheetProperties = new SheetProperties();

		// Add the sheetName to the sheetProperties
		addSheetRequest.setProperties(sheetProperties);
		addSheetRequest.setProperties(sheetProperties.setTitle(sheetName));

		// Create batchUpdateSpreadsheetRequest
		BatchUpdateSpreadsheetRequest batchUpdateSpreadsheetRequest = new BatchUpdateSpreadsheetRequest();

		// Create requestList and set it on the batchUpdateSpreadsheetRequest
		List<Request> requestsList = new ArrayList<Request>();
		batchUpdateSpreadsheetRequest.setRequests(requestsList);

		// Create a new request with containing the addSheetRequest and add it to the
		// requestList
		Request request = new Request();
		request.setAddSheet(addSheetRequest);
		requestsList.add(request);

		// Add the requestList to the batchUpdateSpreadsheetRequest
		batchUpdateSpreadsheetRequest.setRequests(requestsList);
		try {
			// Call the sheets API to execute the batchUpdate
			GoogleUtils.getSheetsService().spreadsheets().batchUpdate(spreadsheetId, batchUpdateSpreadsheetRequest)
					.execute();
		} catch (IOException | GeneralSecurityException exception) {
			throw new GoogleException("There was an unexpected issue while creating a new sheet", exception);
		}
	}
}
