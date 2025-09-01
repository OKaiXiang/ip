import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TaskList {
    private final List<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public TaskList(List<Task> initial) {
        this.tasks = new ArrayList<>(initial);
    }

    public int size() { return tasks.size(); }

    public Task get(int indexZeroBased) { return tasks.get(indexZeroBased); }

    public List<Task> asList() { return tasks; }

    public void add(Task t) { tasks.add(t); }

    public Task removeOneBased(int indexOneBased) {
        return tasks.remove(indexOneBased - 1);
    }

    public Task markOneBased(int indexOneBased) {
        Task t = tasks.get(indexOneBased - 1);
        t.setMark();
        return t;
    }

    public Task unmarkOneBased(int indexOneBased) {
        Task t = tasks.get(indexOneBased - 1);
        t.unMark();
        return t;
    }

    /** Stretch helper: returns tasks that are relevant to a date. */
    public List<Task> occurringOn(LocalDate date) {
        List<Task> out = new ArrayList<>();
        for (Task t : tasks) {
            if (t instanceof Deadline d) {
                if (d.getByDateTime().toLocalDate().equals(date)) out.add(t);
            } else if (t instanceof Event e) {
                var start = e.getStartDateTime().toLocalDate();
                var end   = e.getEndDateTime().toLocalDate();
                if (!date.isBefore(start) && !date.isAfter(end)) out.add(t);
            }
        }
        return out;
    }
}
