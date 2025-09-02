package okuke.task;

import okuke.util.DateTimeUtil;
import java.time.LocalDateTime;

/**
 * A task that must be completed by a specific date/time.
 */
public class Deadline extends Task {
    private final LocalDateTime by;

    /**
     * Creates a Deadline task.
     *
     * @param taskName description/name of the task
     * @param by the deadline date/time (inclusive)
     */
    public Deadline(String taskName, LocalDateTime by) {
        super(taskName);
        this.by = by;
    }

    /**
     * Returns the deadline date/time.
     *
     * @return the "by" timestamp
     */
    public LocalDateTime getByDateTime() {
        return by;
    }

    /**
     * Returns the display form for a Deadline.
     * Example: "[D][ ] return book (by: Aug 06 2025 14:00)"
     *
     * @return formatted deadline string
     */
    @Override
    public String toString() {
        return "[D][" + getStatus() + "] " + getTaskName()
                + " (by: " + DateTimeUtil.formatNice(by) + ")";
    }
}

