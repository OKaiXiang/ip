import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class AddEventCommand extends Command {
    private final String desc;
    private final String fromRaw;
    private final String toRaw;

    public AddEventCommand(String desc, String fromRaw, String toRaw) {
        this.desc = desc;
        this.fromRaw = fromRaw;
        this.toRaw = toRaw;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws OkukeException {
        try {
            LocalDateTime from = DateTimeUtil.parseFlexibleDateTime(fromRaw);
            LocalDateTime to   = DateTimeUtil.parseFlexibleDateTime(toRaw);
            Task t = new Event(desc, from, to); // assumes Level-8 constructor exists
            tasks.add(t);
            ui.showAdded(t, tasks);
            saveOrWarn(storage, tasks);
        } catch (DateTimeParseException dtpe) {
            throw new OkukeException.InvalidCommandException();
        }
    }
}
