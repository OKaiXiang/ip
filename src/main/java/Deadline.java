public class Deadline extends Task{
    private final String deadline;

    public Deadline(String taskName, String deadline) {
        super(taskName);
        this.deadline = deadline;
    }

    public String getType() {
        return "[D]";
    }

    @Override
    public String toString(){
        return this.getType() + super.toString() + "(by: " + this.deadline + ")";
    }
}
