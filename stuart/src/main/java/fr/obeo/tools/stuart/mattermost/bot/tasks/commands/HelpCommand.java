package fr.obeo.tools.stuart.mattermost.bot.tasks.commands;

import java.io.IOException;

import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.CommandDocumentation;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.CommandExecutionContext;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.CommandExecutionException;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.SharedTasksCommand;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.SharedTasksCommandFactory;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.SharedTasksCommandFactory.SharedTasksVerb;

/**
 * {@link SharedTasksCommand} implementation to show the help.
 * 
 * @author lfasani
 *
 */
public class HelpCommand extends SharedTasksCommand {

	public static CommandDocumentation DOCUMENTATION = new CommandDocumentation() {

		@Override
		public String getPurpose() {
			return "Displays the help contents";
		};

		@Override
		public String getUsage(String... taskName) {
			return SharedTasksCommandFactory.COMMAND_STARTER + SharedTasksVerb.HELP.getLabel();
		};
	};

	public HelpCommand(String commandText, String mattermostChannelId) {
		super(commandText, mattermostChannelId);
	}

	@Override
	public void execute(CommandExecutionContext commandExecutionContext) throws CommandExecutionException {
		StringBuilder helpMessage = new StringBuilder();

		helpMessage.append("The task bot allows creating and managing shared tasks\n");
		helpMessage.append(
				"Tasks may be created, registered for, or assigned using a fair distribution algorithm, using the following commands:");
		SharedTasksVerb.getAllVerbDocumentations().stream().forEach(documentation -> {
			helpMessage.append("\n* ");
			helpMessage.append("```" + documentation.getUsage() + "```");
			helpMessage.append(" - ");
			helpMessage.append(documentation.getPurpose());
		});

		try {
			commandExecutionContext.getBot().respond(commandExecutionContext.getPost(), helpMessage.toString());
		} catch (IOException exception) {
			throw new CommandExecutionException(
					"There was an issue while providing the help for channel \"" + this.getChannelId()
							+ "\" and spreadsheet " + commandExecutionContext.getSharedTasksSheetId() + ".",
					exception);
		}
	}
}
