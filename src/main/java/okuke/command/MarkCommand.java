package okuke.command;

import okuke.storage.Storage;
import okuke.task.Task;
import okuke.task.TaskList;
import okuke.ui.Ui;

public class MarkCommand extends Command {
    private final int indexOneBased;
    public MarkCommand(int idx1) { this.indexOneBased = idx1; }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        Task t = tasks.markOneBased(indexOneBased);
        ui.showMark(t);
        saveOrWarn(storage, tasks);
    }
}
