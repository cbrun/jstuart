package fr.obeo.tools.stuart.mattermost.bot.tasks.commands;

import java.io.IOException;

import fr.obeo.tools.stuart.mattermost.MattermostUtils;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.CommandExecutionContext;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.CommandExecutionException;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.CommandWithTaskNameAndChannelIdAndUserId;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.SharedTasksCommand;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.SharedTasksCommandFactory;
import fr.obeo.tools.stuart.mattermost.bot.tasks.google.GoogleException;
import fr.obeo.tools.stuart.mattermost.bot.tasks.google.SharedTasksGoogleUtils;
import fr.obeo.tools.stuart.mattermost.bot.user.MUser;

/**
 * {@link SharedTasksCommand} implementation to show the tasks usage.
 * 
 * @author lfasani
 *
 */
public class HelpCommand extends SharedTasksCommand {

	public HelpCommand(String commandText) {
		super(commandText);
	}

	@Override
	public void execute(CommandExecutionContext commandExecutionContext) throws CommandExecutionException {
		StringBuilder helpMessage = new StringBuilder();

		helpMessage.append("The task bot allows you to create and manage tasks\n");
		helpMessage.append("To do it, use the associated verb with the following synthax\n");
		helpMessage.append("!task <verb> [<args>]\n");
		SharedTasksCommandFactory.ALL_VERBS_INFORMATION.keySet().stream().forEach(verb -> {
			helpMessage.append("\t");
			helpMessage.append(SharedTasksCommandFactory.ALL_VERBS_INFORMATION.get(verb).getUsage(null));
			helpMessage.append("\t");
			helpMessage.append(SharedTasksCommandFactory.ALL_VERBS_INFORMATION.get(verb).getDocumentation());
			helpMessage.append("\n");
		});

		try {
			commandExecutionContext.getBot().respond(commandExecutionContext.getPost(), helpMessage.toString());
		} catch (IOException exception) {
			throw new CommandExecutionException("There was an error while sending the help content", exception);
		}
	}
}
