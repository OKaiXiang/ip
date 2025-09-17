package okuke.command;

import java.util.List;

import okuke.storage.Storage;
import okuke.task.Task;
import okuke.task.TaskList;
import okuke.ui.Ui;

/**
 * Displays all tasks whose description contains a given keyword
 * (case-insensitive).
 *
 * Example:
 *   find book
 *   ____________________________________________________________
 *    Here are the matching tasks in your list:
 *    1.[T][X] read book
 *    2.[D][X] return book (by: June 6th)
 *   ____________________________________________________________
 */
public class FindCommand extends Command {
    private final String keyword;

    /**
     * Creates a find command.
     *
     * @param keyword the search term to match within task descriptions
     */
    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    /**
     * Filters tasks by keyword (case-insensitive), then prints them using
     * the UI header/footer and enumerated list formatting.
     * No persistence or mutation is performed.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        List<Task> matches = tasks.find(keyword);

        if (matches.isEmpty()) {
            // Route through UI so it appears in the JavaFX dialog
            ui.showHelp("  (no matching tasks)");
        } else {
            ui.showItemsHeader("Here are the matching tasks in your list:");
            // Build the numbered list and send as one chunk to the GUI buffer
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < matches.size(); i++) {
                sb.append(String.format(" %d.%s%n", i + 1, matches.get(i)));
            }
            // strip trailing newline for tidier output
            ui.showHelp(sb.toString().stripTrailing());
        }

        ui.showItemsFooter();
    }

}
