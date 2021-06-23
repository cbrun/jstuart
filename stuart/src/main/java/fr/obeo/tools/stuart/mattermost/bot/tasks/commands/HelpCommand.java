package fr.obeo.tools.stuart.mattermost.bot.tasks.commands;

import java.io.IOException;

import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.CommandExecutionContext;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.CommandExecutionException;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.CommandWithTaskNameAndChannelId;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.SharedTasksCommand;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.SharedTasksCommandFactory;

/**
 * {@link SharedTasksCommand} implementation to show the tasks usage.
 * 
 * @author lfasani
 *
 */
public class HelpCommand extends SharedTasksCommand {

	public static CommandWithTaskNameAndChannelId.CommandInformation INFORMATION = new CommandWithTaskNameAndChannelId.CommandInformation() {
		public String getDocumentation() {
			return "To displays the help contents";
		};

		public String getUsage(String taskName) {
			return SharedTasksCommandFactory.COMMAND_STARTER + SharedTasksCommandFactory.VERB_HELP;
		};
	};

	public HelpCommand(String commandText) {
		super(commandText);
	}

	@Override
	public void execute(CommandExecutionContext commandExecutionContext) throws CommandExecutionException {
		StringBuilder helpMessage = new StringBuilder();

		helpMessage.append("The task bot allows creating and managing shared tasks\n");
		helpMessage.append(
				"Tasks may be created, registered for, or assigned using a fair distribution algorithm, using the following commands:");
		SharedTasksCommandFactory.ALL_VERBS_INFORMATION.keySet().stream().forEach(verb -> {
			helpMessage.append("\n* ");
			helpMessage
					.append("```" + SharedTasksCommandFactory.ALL_VERBS_INFORMATION.get(verb).getUsage(null) + "```");
			helpMessage.append("    ");
			helpMessage.append(SharedTasksCommandFactory.ALL_VERBS_INFORMATION.get(verb).getDocumentation());
		});
		// TODO: See if we can list all tasks of the current channel?

		try {
			commandExecutionContext.getBot().respond(commandExecutionContext.getPost(), helpMessage.toString());
		} catch (IOException exception) {
			throw new CommandExecutionException("There was an error while sending the help content", exception);
		}
	}
}
