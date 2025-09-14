package okuke.command;

import okuke.storage.Storage;
import okuke.task.TaskList;
import okuke.ui.Ui;

/**
 * Displays a list of all supported commands and their usage.
 */
public final class HelpCommand extends Command {

    private static final String HELP_TEXT = String.join(System.lineSeparator(),
            "Here are the available commands:",
            "  bye                  : Exit the program",
            "  list                 : Show all tasks",
            "  mark <index>         : Mark a task as done (1-based index)",
            "  unmark <index>       : Unmark a task (1-based index)",
            "  delete <index>       : Delete a task (1-based index)",
            "  todo <desc>          : Add a todo task",
            "  deadline <desc> /by <yyyy-MM-dd HH:mm>",
            "                       : Add a deadline task",
            "  event <desc> /from <yyyy-MM-dd HH:mm> /to <yyyy-MM-dd HH:mm>",
            "                       : Add an event task",
            "  on <yyyy-MM-dd>      : Show tasks occurring on a specific date",
            "  find <keyword>       : Find tasks containing the keyword",
            "  help                 : Show this help message"
    );

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showHelp(HELP_TEXT);
    }
}
