package okuke.command;

import java.time.LocalDate;
import okuke.util.DateTimeUtil;
import okuke.storage.Storage;
import okuke.task.Task;
import okuke.task.TaskList;
import okuke.ui.Ui;

/**
 * Displays all tasks occurring on a given date (deadlines on that date, or
 * events whose interval includes that date).
 */
public class OnDateCommand extends Command {
    private final String dateRaw;

    /**
     * Creates an on-date filter command.
     *
     * @param dateRaw user input for the date (flexible formats)
     */
    public OnDateCommand(String dateRaw) { this.dateRaw = dateRaw; }

    /**
     * Parses {@code dateRaw} into a date, filters tasks with
     * {@link okuke.task.TaskList#occurringOn(java.time.LocalDate)}, and prints them
     * using the UI header/footer. No persistence or mutation is performed.
     *
     * @throws java.time.format.DateTimeParseException if the date cannot be parsed
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        LocalDate date = DateTimeUtil.parseFlexibleDateTime(dateRaw).toLocalDate();
        var items = tasks.occurringOn(date);
        ui.showItemsHeader("Items on " + date + ":");
        if (items.isEmpty()) {
            System.out.println("  (none)");
        } else {
            for (Task t : items) {
                System.out.println(" - " + t);
            }
        }
        ui.showItemsFooter();
    }
}
