public class DeleteCommand extends Command {
    private final int indexOneBased;
    public DeleteCommand(int idx1) { this.indexOneBased = idx1; }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        Task removed = tasks.removeOneBased(indexOneBased);
        ui.showDeleted(removed, tasks);
        saveOrWarn(storage, tasks);
    }
}
