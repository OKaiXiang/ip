package okuke.task;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Mutable list wrapper around tasks with convenience helpers
 * for 1-based operations and date-based filtering.
 */
public class TaskList {
    private final List<Task> tasks;

    /**
     * Creates an empty task list.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a task list initialized with the given tasks.
     * A defensive copy of the provided list is made.
     *
     * @param initial the initial tasks to include
     */
    public TaskList(List<Task> initial) {
        this.tasks = new ArrayList<>(initial);
    }

    public int size() { return tasks.size(); }

    /**
     * Returns the task at the specified zero-based index.
     *
     * @param indexZeroBased index starting at 0
     * @return the task at the given position
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public Task get(int indexZeroBased) {
        assert indexZeroBased >= 0 && indexZeroBased < tasks.size() : "index OOB";
        return tasks.get(indexZeroBased);
    }

    /**
     * Returns the underlying modifiable list view.
     * Mutations on the returned list will reflect in this TaskList.
     *
     * @return the backing list of tasks
     */
    public List<Task> asList() { return tasks; }

    public void add(Task t) {
        assert t != null : "cannot add null task";
        tasks.add(t);
    }

    /**
     * Removes and returns the task at the given 1-based position.
     *
     * @param indexOneBased position starting at 1
     * @return the removed task
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public Task removeOneBased(int indexOneBased) {
        assert indexOneBased >= 1 && indexOneBased <= tasks.size() : "index OOB";
        return tasks.remove(indexOneBased - 1);
    }

    /**
     * Marks the task at the given 1-based position as done and returns it.
     *
     * @param indexOneBased position starting at 1
     * @return the task after being marked
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public Task markOneBased(int indexOneBased) {
        Task t = tasks.get(indexOneBased - 1);
        t.setMark();
        return t;
    }

    /**
     * Unmarks the task at the given 1-based position and returns it.
     *
     * @param indexOneBased position starting at 1
     * @return the task after being unmarked
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public Task unmarkOneBased(int indexOneBased) {
        Task t = tasks.get(indexOneBased - 1);
        t.unMark();
        assert !t.isDone() : "task should be unmarked"
        return t;
    }

    /**
    * Returns tasks that are relevant to the specified date:
    * <ul>
    *   <li><b>Deadline</b>: included if its "by" date equals {@code date}.</li>
    *   <li><b>Event</b>: included if {@code date} falls in [start, end] (inclusive on both ends).</li>
    *   <li>Other task types are ignored.</li>
    * </ul>
    *
    * @param date the calendar date to match
    * @return a new list of tasks occurring on the given date
    */
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

    /**
     * Returns tasks whose description contains the given keyword,
     * case-insensitive.
     *
     * @param keyword search string to find within task names
     * @return a new list containing matching tasks in their original order
     */
    public List<Task> find(String keyword) {
        String needle = keyword == null ? "" : keyword.toLowerCase();
        List<Task> result = new ArrayList<>();
        for (Task t : tasks) {
            if (t.getTaskName().toLowerCase().contains(needle)) {
                result.add(t);
            }
        }
        return result;
    }

}
