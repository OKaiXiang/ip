package okuke.command;

import okuke.storage.Storage;
import okuke.task.TaskList;
import okuke.ui.Ui;

/**
 * Exits the application after printing the goodbye message.
 */
public class ExitCommand extends Command {

    /**
     * Prints the goodbye message. No persistence is performed.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showBye();
    }

    /**
     * Always signals that the application should terminate.
     *
     * @return {@code true}
     */
    @Override
    public boolean isExit() {
        return true;
    }
}
