import java.time.LocalDate;

public class OnDateCommand extends Command {
    private final String dateRaw;
    public OnDateCommand(String dateRaw) { this.dateRaw = dateRaw; }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        LocalDate date = DateTimeUtil.parseFlexibleDateTime(dateRaw).toLocalDate();
        var items = tasks.occurringOn(date);
        ui.showItemsHeader("Items on " + date + ":");
        if (items.isEmpty()) {
            System.out.println("  (none)");
        } else {
            for (Task t : items) {
                System.out.println(" - " + t);
            }
        }
        ui.showItemsFooter();
    }
}
