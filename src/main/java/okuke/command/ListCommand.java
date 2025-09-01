package okuke.command;

import okuke.storage.Storage;
import okuke.task.TaskList;
import okuke.ui.Ui;

public class ListCommand extends Command {
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showList(tasks);
    }
}
