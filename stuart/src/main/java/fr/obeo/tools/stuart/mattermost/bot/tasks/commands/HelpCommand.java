package fr.obeo.tools.stuart.mattermost.bot.tasks.commands;

import java.io.IOException;
import java.util.List;

import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.CommandExecutionContext;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.CommandExecutionException;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.CommandDocumentation;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.SharedTasksCommand;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.SharedTasksCommandFactory;
import fr.obeo.tools.stuart.mattermost.bot.tasks.google.GoogleException;
import fr.obeo.tools.stuart.mattermost.bot.tasks.google.SharedTasksGoogleUtils;

/**
 * {@link SharedTasksCommand} implementation to show the tasks usage.
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
			return SharedTasksCommandFactory.COMMAND_STARTER + SharedTasksCommandFactory.VERB_HELP;
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
		SharedTasksCommandFactory.ALL_VERBS_DOCUMENTATION.keySet().stream().forEach(verb -> {
			helpMessage.append("\n* ");
			helpMessage
					.append("```" + SharedTasksCommandFactory.ALL_VERBS_DOCUMENTATION.get(verb).getUsage(null) + "```");
			helpMessage.append(" - ");
			helpMessage.append(SharedTasksCommandFactory.ALL_VERBS_DOCUMENTATION.get(verb).getPurpose());
		});

		try {
			List<String> namesOfTasksAvailableInChannel = SharedTasksGoogleUtils
					.getAllTasksOfChannel(commandExecutionContext.getSharedTasksSheetId(), this.getChannelId());
			helpMessage.append("\n");
			if (namesOfTasksAvailableInChannel.isEmpty()) {
				helpMessage.append("There are no tasks for this channel. To create a task, use command ```"
						+ SharedTasksCommandFactory.ALL_VERBS_DOCUMENTATION.get(SharedTasksCommandFactory.VERB_CREATE)
								.getUsage(null)
						+ "```.");
			} else {
				helpMessage.append("There " + (namesOfTasksAvailableInChannel.size() == 1 ? "is" : "are") + " "
						+ namesOfTasksAvailableInChannel.size() + " task"
						+ (namesOfTasksAvailableInChannel.size() == 1 ? "" : "s") + " for this channel:");
				namesOfTasksAvailableInChannel.stream().forEach(taskName -> {
					helpMessage.append("\n* ");
					helpMessage.append(taskName);
				});
			}
			commandExecutionContext.getBot().respond(commandExecutionContext.getPost(), helpMessage.toString());
		} catch (IOException | GoogleException exception) {
			throw new CommandExecutionException(
					"There was an issue while providing the help for channel \"" + this.getChannelId()
							+ "\" and spreadsheet " + commandExecutionContext.getSharedTasksSheetId() + ".",
					exception);
		}
	}
}
