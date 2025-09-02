package okuke.command;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import okuke.exception.OkukeException;
import okuke.storage.Storage;
import okuke.task.Deadline;
import okuke.task.Task;
import okuke.task.TaskList;
import okuke.ui.Ui;
import okuke.util.DateTimeUtil;

/**
 * Adds a new {@link okuke.task.Deadline} with a "by" date/time.
 */
public class AddDeadlineCommand extends Command {
    private final String desc;
    private final String byRaw;

    /**
     * Creates an add-deadline command.
     *
     * @param desc  task description
     * @param byRaw user input for the deadline date/time (flexible formats)
     */
    public AddDeadlineCommand(String desc, String byRaw) {
        this.desc = desc;
        this.byRaw = byRaw;
    }

    /**
     * Parses {@code byRaw}, constructs a {@code Deadline}, appends it to the list,
     * shows a confirmation, and saves to storage.
     *
     * @throws okuke.exception.OkukeException.InvalidCommandException if {@code byRaw} cannot be parsed
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws OkukeException {
        try {
            LocalDateTime by = DateTimeUtil.parseFlexibleDateTime(byRaw);
            Task t = new Deadline(desc, by); // assumes Level-8 constructor exists
            tasks.add(t);
            ui.showAdded(t, tasks);
            saveOrWarn(storage, tasks);
        } catch (DateTimeParseException dtpe) {
            throw new OkukeException.InvalidCommandException(); // reuse your friendly message
        }
    }
}
