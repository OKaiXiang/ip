package okuke.task;

import okuke.util.DateTimeUtil;
import java.time.LocalDateTime;

/**
 * A task spanning a time interval between a start and an end.
 */
public class Event extends Task {
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;

    /**
     * Creates an Event task.
     *
     * @param taskName description/name of the event
     * @param startDate start date/time of the event (inclusive)
     * @param endDate end date/time of the event (inclusive)
     */
    public Event(String taskName, LocalDateTime startDate, LocalDateTime endDate) {
        super(taskName);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /**
     * Returns the event start date/time.
     *
     * @return start timestamp
     */
    public LocalDateTime getStartDateTime() {
        return this.startDate;
    }

    /**
     * Returns the event end date/time.
     *
     * @return end timestamp
     */
    public LocalDateTime getEndDateTime() {
        return this.endDate;
    }

    /**
     * Returns the display form for an Event.
     * Example: "[E][X] project meeting (from: Aug 06 2025 14:00 to: Aug 06 2025 16:00)"
     *
     * @return formatted event string
     */
    @Override
    public String toString() {
        return "[E][" + getStatus() + "] " + getTaskName()
                + " (from: " + DateTimeUtil.formatNice(startDate)
                + " to: "   + DateTimeUtil.formatNice(endDate) + ")";
    }
}
