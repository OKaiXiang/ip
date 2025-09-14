package okuke.ui;

import okuke.task.Task;
import okuke.task.TaskList;

import java.util.Scanner;

/**
 * Handles all user-facing console I/O for OKuke.
 * Reads commands and prints formatted responses.
 */
public class Ui {
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Prints the welcome banner.
     */
    public void showWelcome() {
        System.out.println("Hello! I'm OKuke.");
        System.out.println("What can I do for you?");
        showLine();
    }

    /**
     * Prints the goodbye message.
     */
    public void showBye() {
        System.out.println("Bye. Hope to see you again soon!");
    }

    /**
     * Reads a line of user input from the console.
     * Leading/trailing whitespace may be trimmed by the caller/implementation.
     *
     * @return the raw command string entered by the user
     */
    public String readCommand() {
        return scanner.nextLine().trim();
    }

    /**
     * Prints a visual divider line separating UI sections.
     */
    public void showLine() {
        System.out.println("____________________________________________________________");
    }

    /**
     * Prints a startup/loading error message.
     *
     * @param msg the error text to display
     */
    public void showLoadingError(String msg) {
        showLine();
        System.out.println(msg);
        showLine();
    }

    /**
     * Prints an error message produced during command processing.
     *
     * @param message the error text to display
     */
    public void showError(String message) {
        showLine();
        System.out.println(message);
        showLine();
    }

    /**
     * Prints a confirmation that a task was added,
     * followed by the current task count.
     *
     * @param task the task that was added
     * @param tasks the list containing all tasks (for count)
     */
    public void showAdded(Task task, TaskList tasks) {
        showLine();
        System.out.println("added: " + task.getTaskName());
        System.out.println("Now you have " + tasks.size() + " tasks in the list.");
        showLine();
    }

    /**
     * Prints a confirmation that a task was deleted,
     * followed by the current task count.
     *
     * @param removed the task that was removed
     * @param tasks the list containing the remaining tasks (for count)
     */
    public void showDeleted(Task removed, TaskList tasks) {
        showLine();
        System.out.println("Noted. I've removed this okuke.task:");
        System.out.println("  " + removed);
        System.out.println("Now you have " + tasks.size() + " tasks in the list.");
        showLine();
    }

    /**
     * Prints a confirmation that a task has been marked done.
     *
     * @param t the task that was marked
     */
    public void showMark(Task t) {
        showLine();
        System.out.println("Nice! I've marked this okuke.task as done:");
        System.out.println("  " + t);
        showLine();
    }

    /**
     * Prints a confirmation that a task has been unmarked.
     *
     * @param t the task that was unmarked
     */
    public void showUnmark(Task t) {
        showLine();
        System.out.println("OK, I've marked this okuke.task as not done yet:");
        System.out.println("  " + t);
        showLine();
    }

    /**
     * Prints the current list of tasks in order.
     * If empty, prints a friendly message instead.
     *
     * @param tasks the task list to display
     */
    public void showList(TaskList tasks) {
        showLine();
        if (tasks.size() == 0) {
            System.out.println("Your list is empty.");
        } else {
            System.out.println("Here are the tasks in your list:");
            for (int i = 0; i < tasks.size(); i++) {
                System.out.printf("%d.%s%n", i + 1, tasks.get(i));
            }
        }
        showLine();
    }

    public void showHelp(String msg) {
        System.out.println(msg);
    }

    /**
     * Prints a divider line and a section header before a list of items.
     *
     * @param title the header text to print
     */
    public void showItemsHeader(String title) {
        showLine();
        System.out.println(title);
    }

    /**
     * Prints the closing divider line after a list of items.
     */
    public void showItemsFooter() {
        showLine();
    }
}
