package okuke.task;

/**
 * A simple to-do task with no date/time component.
 */
public class Todo extends Task {

    /**
     * Creates a Todo task.
     *
     * @param taskName description/name of the to-do
     */
    public Todo(String taskName) {
        super(taskName);
    }

    /**
     * Returns the task type tag for Todos.
     *
     * @return "[T]"
     */
    public String getType() {
        return "[T]";
    }

    /**
     * Returns the display form for a Todo.
     * Example: "[T][X] read book"
     *
     * @return type-prefixed display string
     */
    @Override
    public String toString() {
        return this.getType() + super.toString();
    }
}
