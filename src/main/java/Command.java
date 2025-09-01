public abstract class Command {
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws Exception;
    public boolean isExit() { return false; }

    /** Call this inside mutating commands to persist changes safely. */
    protected void saveOrWarn(Storage storage, TaskList tasks) {
        try {
            storage.save(tasks.asList());
        } catch (Exception e) {
            System.err.println("[Storage] Failed to save: " + e.getMessage());
        }
    }
}
