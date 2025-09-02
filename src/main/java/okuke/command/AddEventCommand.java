package okuke.command;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import okuke.exception.OkukeException;
import okuke.storage.Storage;
import okuke.task.Event;
import okuke.task.Task;
import okuke.task.TaskList;
import okuke.ui.Ui;
import okuke.util.DateTimeUtil;

/**
 * Adds a new {@link okuke.task.Event} spanning a start and end date/time.
 */
public class AddEventCommand extends Command {
    private final String desc;
    private final String fromRaw;
    private final String toRaw;

    /**
     * Creates an add-event command.
     *
     * @param desc    event description
     * @param fromRaw user input for the start date/time (flexible formats)
     * @param toRaw   user input for the end date/time (flexible formats)
     */
    public AddEventCommand(String desc, String fromRaw, String toRaw) {
        this.desc = desc;
        this.fromRaw = fromRaw;
        this.toRaw = toRaw;
    }

    /**
     * Parses {@code fromRaw} and {@code toRaw}, constructs an {@code Event},
     * appends it to the list, shows a confirmation, and saves to storage.
     *
     * @throws okuke.exception.OkukeException.InvalidCommandException if either date/time cannot be parsed
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws OkukeException {
        try {
            LocalDateTime from = DateTimeUtil.parseFlexibleDateTime(fromRaw);
            LocalDateTime to   = DateTimeUtil.parseFlexibleDateTime(toRaw);
            Task t = new Event(desc, from, to); // assumes Level-8 constructor exists
            tasks.add(t);
            ui.showAdded(t, tasks);
            saveOrWarn(storage, tasks);
        } catch (DateTimeParseException dtpe) {
            throw new OkukeException.InvalidCommandException();
        }
    }
}
