public class Event extends Task {
    private final String startDate;
    private final String endDate;

    public Event(String taskName, String startDate, String endDate) {
        super(taskName);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getType() {
        return "[E]";
    }

    public String getStartDate() {
        return this.startDate;
    }

    public String getEndDate() {
        return this.endDate;
    }

    @Override
    public String toString() {
        return this.getType() + super.toString() + "(from: " + this.startDate + " to: " + this.endDate + ")";
    }
}
