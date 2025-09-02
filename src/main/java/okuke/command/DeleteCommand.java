package okuke.command;

import okuke.storage.Storage;
import okuke.task.Task;
import okuke.task.TaskList;
import okuke.ui.Ui;

/**
 * Deletes a task at a given 1-based index.
 */
public class DeleteCommand extends Command {
    private final int indexOneBased;

    /**
     * Creates a delete command.
     *
     * @param idx1 the 1-based index of the task to remove
     */
    public DeleteCommand(int idx1) { this.indexOneBased = idx1; }

    /**
     * Removes the task at {@code indexOneBased}, shows a confirmation,
     * and saves the updated list to storage.
     *
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        Task removed = tasks.removeOneBased(indexOneBased);
        ui.showDeleted(removed, tasks);
        saveOrWarn(storage, tasks);
    }
}
