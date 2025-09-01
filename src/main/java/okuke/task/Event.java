package okuke.task;

import okuke.util.DateTimeUtil;
import java.time.LocalDateTime;

public class Event extends Task {
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;

    public Event(String taskName, LocalDateTime startDate, LocalDateTime endDate) {
        super(taskName);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public LocalDateTime getStartDateTime() {
        return this.startDate;
    }

    public LocalDateTime getEndDateTime() {
        return this.endDate;
    }

    @Override
    public String toString() {
        return "[E][" + getStatus() + "] " + getTaskName()
                + " (from: " + DateTimeUtil.formatNice(startDate)
                + " to: "   + DateTimeUtil.formatNice(endDate) + ")";
    }
}
