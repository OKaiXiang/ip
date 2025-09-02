package okuke.command;

import okuke.storage.Storage;
import okuke.task.Task;
import okuke.task.TaskList;
import okuke.ui.Ui;

/**
 * Clears the completion state of a task at a given 1-based index.
 */
public class UnmarkCommand extends Command {
    private final int indexOneBased;

    /**
     * Creates an unmark command.
     *
     * @param idx1 the 1-based index of the task to unmark
     */
    public UnmarkCommand(int idx1) { this.indexOneBased = idx1; }

    /**
     * Unmarks the task, shows a confirmation, and saves the updated list to storage.
     *
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        Task t = tasks.unmarkOneBased(indexOneBased);
        ui.showUnmark(t);
        saveOrWarn(storage, tasks);
    }
}
