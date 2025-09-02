package okuke.command;

import okuke.storage.Storage;
import okuke.task.Task;
import okuke.task.TaskList;
import okuke.ui.Ui;

/**
 * Marks a task as done at a given 1-based index.
 */
public class MarkCommand extends Command {
    private final int indexOneBased;

    /**
     * Creates a mark command.
     *
     * @param idx1 the 1-based index of the task to mark
     */
    public MarkCommand(int idx1) { this.indexOneBased = idx1; }

    /**
     * Marks the task, shows a confirmation, and saves the updated list to storage.
     *
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        Task t = tasks.markOneBased(indexOneBased);
        ui.showMark(t);
        saveOrWarn(storage, tasks);
    }
}
