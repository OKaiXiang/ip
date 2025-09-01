import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class AddDeadlineCommand extends Command {
    private final String desc;
    private final String byRaw;

    public AddDeadlineCommand(String desc, String byRaw) {
        this.desc = desc;
        this.byRaw = byRaw;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws OkukeException {
        try {
            LocalDateTime by = DateTimeUtil.parseFlexibleDateTime(byRaw);
            Task t = new Deadline(desc, by); // assumes Level-8 constructor exists
            tasks.add(t);
            ui.showAdded(t, tasks);
            saveOrWarn(storage, tasks);
        } catch (DateTimeParseException dtpe) {
            throw new OkukeException.InvalidCommandException(); // reuse your friendly message
        }
    }
}
