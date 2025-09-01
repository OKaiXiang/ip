package okuke.ui;

import okuke.task.Task;
import okuke.task.TaskList;
import java.util.Scanner;

public class Ui {
    private final Scanner scanner = new Scanner(System.in);

    public void showWelcome() {
        System.out.println("Hello! I'm OKuke.");
        System.out.println("What can I do for you?");
        showLine();
    }

    public void showBye() {
        System.out.println("Bye. Hope to see you again soon!");
    }

    public String readCommand() {
        return scanner.nextLine().trim();
    }

    public void showLine() {
        System.out.println("____________________________________________________________");
    }

    public void showLoadingError(String msg) {
        showLine();
        System.out.println(msg);
        showLine();
    }

    public void showError(String message) {
        showLine();
        System.out.println(message);
        showLine();
    }

    public void showAdded(Task task, TaskList tasks) {
        showLine();
        System.out.println("added: " + task.getTaskName());
        System.out.println("Now you have " + tasks.size() + " tasks in the list.");
        showLine();
    }

    public void showDeleted(Task removed, TaskList tasks) {
        showLine();
        System.out.println("Noted. I've removed this okuke.task:");
        System.out.println("  " + removed);
        System.out.println("Now you have " + tasks.size() + " tasks in the list.");
        showLine();
    }

    public void showMark(Task t) {
        showLine();
        System.out.println("Nice! I've marked this okuke.task as done:");
        System.out.println("  " + t);
        showLine();
    }

    public void showUnmark(Task t) {
        showLine();
        System.out.println("OK, I've marked this okuke.task as not done yet:");
        System.out.println("  " + t);
        showLine();
    }

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

    public void showItemsHeader(String title) {
        showLine();
        System.out.println(title);
    }

    public void showItemsFooter() {
        showLine();
    }
}
