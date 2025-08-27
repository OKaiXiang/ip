public class Task {
    private final String taskName;
    private boolean isMark;

    public Task(String taskName) {
        this.taskName = taskName;
        this.isMark = false;
    }

    /*
    public Task(String taskName, boolean isMark) {
        this.taskName = taskName;
        this.isMark = isMark;
    }

    public String getTaskName() {
        return this.taskName;
    }

    public boolean isMark() {
        return this.isMark;
    }
    */

    public void setMark() {
        this.isMark = true;
    }

    public void unMark() {
        this.isMark = false;
    }

    public String getStatus() {
        if (this.isMark) {
            return "X";
        }
        return " ";
    }

    @Override
    public String toString() {
        return String.format("[%s] %s", getStatus(), this.taskName);
    }
}
