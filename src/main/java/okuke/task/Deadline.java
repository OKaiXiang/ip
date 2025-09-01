package okuke.task;

import okuke.util.DateTimeUtil;
import java.time.LocalDateTime;

public class Deadline extends Task {
    private final LocalDateTime by;

    public Deadline(String taskName, LocalDateTime by) {
        super(taskName);
        this.by = by;
    }

    public LocalDateTime getByDateTime() {
        return by;
    }

    @Override
    public String toString() {
        return "[D][" + getStatus() + "] " + getTaskName()
                + " (by: " + DateTimeUtil.formatNice(by) + ")";
    }
}

