public class Todo extends Task {
    public Todo(String taskName) {
        super(taskName);
    }

    public String getType() {
        return "[T]";
    }

    @Override
    public String toString() {
        return this.getType() + super.toString();
    }
}
