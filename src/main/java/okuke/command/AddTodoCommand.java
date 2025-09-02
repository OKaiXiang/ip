package okuke.command;

import okuke.storage.Storage;
import okuke.task.Todo;
import okuke.task.Task;
import okuke.task.TaskList;
import okuke.ui.Ui;

/**
 * Adds a new {@link okuke.task.Todo} to the task list.
 */
public class AddTodoCommand extends Command {
    private final String desc;

    /**
     * Creates an add-todo command.
     *
     * @param desc the description of the to-do item
     */
    public AddTodoCommand(String desc) { this.desc = desc; }

    /**
     * Creates a {@code Todo}, appends it to the list, shows a confirmation,
     * and saves the updated list to storage.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        Task t = new Todo(desc);
        tasks.add(t);
        ui.showAdded(t, tasks);
        saveOrWarn(storage, tasks);
    }
}
