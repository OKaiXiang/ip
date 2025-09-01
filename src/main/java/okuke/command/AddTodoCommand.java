package okuke.command;

import okuke.storage.Storage;
import okuke.task.Todo;
import okuke.task.Task;
import okuke.task.TaskList;
import okuke.ui.Ui;

public class AddTodoCommand extends Command {
    private final String desc;
    public AddTodoCommand(String desc) { this.desc = desc; }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        Task t = new Todo(desc);
        tasks.add(t);
        ui.showAdded(t, tasks);
        saveOrWarn(storage, tasks);
    }
}
