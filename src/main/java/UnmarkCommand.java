public class UnmarkCommand extends Command {
    private final int indexOneBased;
    public UnmarkCommand(int idx1) { this.indexOneBased = idx1; }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        Task t = tasks.unmarkOneBased(indexOneBased);
        ui.showUnmark(t);
        saveOrWarn(storage, tasks);
    }
}
