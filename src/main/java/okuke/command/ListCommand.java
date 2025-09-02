package okuke.command;

import okuke.storage.Storage;
import okuke.task.TaskList;
import okuke.ui.Ui;

/**
 * Displays the current list of tasks.
 */
public class ListCommand extends Command {

    /**
     * Delegates to {@link okuke.ui.Ui#showList(okuke.task.TaskList)}.
     * No persistence or mutation is performed.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showList(tasks);
    }
}
