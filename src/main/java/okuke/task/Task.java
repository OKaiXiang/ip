package okuke.task;

/**
 * Represents a generic task with a name and completion status.
 * Immutable name; mutable completion flag.
 */
public class Task {
    private final String taskName;
    private boolean isMark;

    /**
     * Constructs a new task marked as not done.
     *
     * @param taskName the textual description/name of the task
     */
    public Task(String taskName) {
        this.taskName = taskName;
        this.isMark = false;
    }

    /**
     * Returns the task's name
     *
     * @return the task name
     */
    public String getTaskName() {
        return this.taskName;
    }

    /**
     * Marks this task as completed/done.
     */
    public void setMark() {
        this.isMark = true;
    }

    /**
     * Clears the completion mark, setting the task to "not done".
     */
    public void unMark() {
        this.isMark = false;
    }

    /**
     * Returns the status flag used in textual renderings.
     *
     * @return "X" if marked complete, otherwise a single space " "
     */
    public String getStatus() {
        if (this.isMark) {
            return "X";
        }
        return " ";
    }

    /**
     * Returns a compact string form used in list displays.
     * Format: "[<status>] <name>", where status is "X" or " ".
     *
     * @return display string for this task
     */
    @Override
    public String toString() {
        return String.format("[%s] %s", getStatus(), this.taskName);
    }
}
