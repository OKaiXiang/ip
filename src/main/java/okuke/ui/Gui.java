package okuke.ui;

import okuke.task.Task;
import okuke.task.TaskList;

/**
 * UI that collects messages into a buffer for the JavaFX dialog, instead of printing to stdout.
 */
public class Gui extends Ui {
    private final StringBuilder out = new StringBuilder();

    private void line() {
        out.append("____________________________________________________________\n");
    }

    /** Returns and clears the buffered output. */
    public String consume() {
        String s = out.toString().trim();
        out.setLength(0);
        return s.isEmpty() ? "(no output)" : s;
    }

    // --- overrides mapping your existing Ui text to strings ---

    @Override
    public void showWelcome() {
        // (Usually we skip the banner in GUI to avoid spamming on startup)
        // out.append("Hello! I'm OKuke.\nWhat can I do for you?\n"); line();
    }

    @Override
    public void showBye() {
        out.append("Bye. Hope to see you again soon!\n");
    }

    @Override
    public void showLoadingError(String msg) { line(); out.append(msg).append('\n'); line(); }

    @Override
    public void showError(String message) { line(); out.append(message).append('\n'); line(); }

    @Override
    public void showAdded(Task task, TaskList tasks) {
        line();
        out.append("added: ").append(task.getTaskName()).append('\n');
        out.append("Now you have ").append(tasks.size()).append(" tasks in the list.\n");
        line();
    }

    @Override
    public void showDeleted(Task removed, TaskList tasks) {
        line();
        out.append("Noted. I've removed this task:\n  ").append(removed).append('\n');
        out.append("Now you have ").append(tasks.size()).append(" tasks in the list.\n");
        line();
    }

    @Override
    public void showMark(Task t) { line(); out.append("Nice! I've marked this task as done:\n  ").append(t).append('\n'); line(); }

    @Override
    public void showUnmark(Task t) { line(); out.append("OK, I've marked this task as not done yet:\n  ").append(t).append('\n'); line(); }

    @Override
    public void showList(TaskList tasks) {
        line();
        if (tasks.size() == 0) {
            out.append("Your list is empty.\n");
        } else {
            out.append("Here are the tasks in your list:\n");
            for (int i = 0; i < tasks.size(); i++) {
                out.append(String.format("%d.%s%n", i + 1, tasks.get(i)));
            }
        }
        line();
    }

    @Override public void showItemsHeader(String title) { line(); out.append(title).append('\n'); }
    @Override public void showItemsFooter() { line(); }
}
