package fr.obeo.tools.stuart.mattermost.bot.tasks.commands;

import java.io.IOException;
import java.util.List;

import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.CommandDocumentation;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.CommandExecutionContext;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.CommandExecutionException;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.SharedTasksCommand;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.SharedTasksCommandFactory;
import fr.obeo.tools.stuart.mattermost.bot.tasks.commands.common.SharedTasksCommandFactory.SharedTasksVerb;
import fr.obeo.tools.stuart.mattermost.bot.tasks.google.GoogleException;
import fr.obeo.tools.stuart.mattermost.bot.tasks.google.SharedTasksGoogleUtils;

/**
 * {@link SharedTasksCommand} implementation to show the lists of existing tasks
 * of the channel.
 * 
 * TODO: do we want to display the last done timestamp or number of users
 * alongside the task name?
 * 
 * @author flatombe
 *
 */
public class ListCommand extends SharedTasksCommand {

	public static CommandDocumentation DOCUMENTATION = new CommandDocumentation() {

		@Override
		public String getPurpose() {
			return "Displays the existing tasks of this channel";
		};

		@Override
		public String getUsage(String... taskName) {
			return SharedTasksCommandFactory.COMMAND_STARTER + SharedTasksVerb.LIST.getLabel();
		};
	};

	public ListCommand(String commandText, String mattermostChannelId) {
		super(commandText, mattermostChannelId);
	}

	@Override
	public void execute(CommandExecutionContext commandExecutionContext) throws CommandExecutionException {
		try {
			StringBuilder messageBuilder = new StringBuilder();

			List<String> namesOfTasksAvailableInChannel = SharedTasksGoogleUtils
					.getAllTasksOfChannel(commandExecutionContext.getSharedTasksSheetId(), this.getChannelId());
			messageBuilder.append("\n");
			if (namesOfTasksAvailableInChannel.isEmpty()) {
				messageBuilder.append("There are no tasks for this channel. To create a task, use command ```"
						+ SharedTasksVerb.CREATE.getDocumentation().getUsage() + "```.");
			} else {
				messageBuilder.append("There " + (namesOfTasksAvailableInChannel.size() == 1 ? "is" : "are") + " "
						+ namesOfTasksAvailableInChannel.size() + " task"
						+ (namesOfTasksAvailableInChannel.size() == 1 ? "" : "s") + " for this channel:");
				namesOfTasksAvailableInChannel.stream().forEach(taskName -> {
					messageBuilder.append("\n* ");
					messageBuilder.append(taskName);
				});
			}
			commandExecutionContext.getBot().respond(commandExecutionContext.getPost(), messageBuilder.toString());
		} catch (IOException | GoogleException exception) {
			throw new CommandExecutionException(
					"There was an issue while providing the help for channel \"" + this.getChannelId()
							+ "\" and spreadsheet " + commandExecutionContext.getSharedTasksSheetId() + ".",
					exception);
		}
	}
}
