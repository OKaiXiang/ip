package okuke.command;

import okuke.storage.Storage;
import okuke.task.TaskList;
import okuke.ui.Ui;

/**
 * Base type for all executable commands in OKuke.
 * Subclasses implement {@link #execute(TaskList, Ui, Storage)} to perform work.
 */
public abstract class Command {

    /**
     * Executes this command against the given model and UI.
     *
     * @param tasks   the in-memory task list to read and/or mutate
     * @param ui      the UI used to render output
     * @param storage the storage used by mutating commands to persist changes
     * @throws Exception if the command encounters a fatal error while executing
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws Exception;

    /**
     * Indicates whether running this command should terminate the app.
     *
     * @return {@code true} if the REPL should exit after this command; otherwise {@code false}
     */
    public boolean isExit() { return false; }

    /**
     * Persists the current tasks to disk; logs (to stderr) if saving fails.
     * Intended for use by mutating commands after state changes.
     *
     * @param storage storage instance responsible for saving
     * @param tasks   the tasks to persist
     */
    protected void saveOrWarn(Storage storage, TaskList tasks) {
        try {
            storage.save(tasks.asList());
        } catch (Exception e) {
            System.err.println("[okuke.storage.Storage] Failed to save: " + e.getMessage());
        }
    }
}
