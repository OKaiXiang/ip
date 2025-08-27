import java.util.Scanner;

public class OKuke {
    public static void main(String[] args) {
        Task[] list = new Task[100];
        greetings();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String line = scanner.nextLine().trim();
            String[] parts = line.split(" ");
            if (line.equals("bye")) {
                goodbye();
                return;
            }
            if (line.equals("list")) {
                listArray(list);
                continue;
            }
            if (parts[0].equals("mark")) {
                int markedNum = Integer.parseInt(parts[1]);
                mark(list, markedNum);
                continue;
            }
            if (parts[0].equals("unmark")) {
                int unmarkedNum = Integer.parseInt(parts[1]);
                unmark(list, unmarkedNum);
                continue;
            }
            addArray(list, line);
            //printEcho(line);
        }
    }

    public static void mark(Task[] list, int input) {
        if (input > 100 || input < 1 || list[input - 1] == null) {
            System.out.println(" Task does not exist");
            return;
        }
        list[input-1].setMark();
        System.out.println("____________________________________________________________");
        System.out.println("Nice! I've marked this task as done:");
        System.out.println(list[input-1].toString());
        System.out.println("____________________________________________________________");
    }

    public static void unmark(Task[] list, int input) {
        if (input > 100 || input < 1 || list[input - 1] == null) {
            System.out.println(" Task does not exist");
            return;
        }
        list[input-1].unMark();
        System.out.println("____________________________________________________________");
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println(list[input-1].toString());
        System.out.println("____________________________________________________________");
    }

    public static void addArray(Task[] input, String message) {
        String[] parts = message.split(" ", 2); // split into command + rest
        String command = parts[0];
        String description = (parts.length > 1) ? parts[1] : "";

        Task newTask;

        switch (command) {
            case "todo":
                if (description.isEmpty()) {
                    System.out.println("☹ OOPS!!! The description of a todo cannot be empty.");
                    return;
                }
                newTask = new Todo(description);
                break;

            case "deadline":
                // expect format: deadline <desc> /by <date>
                String[] deadlineParts = description.split("/by", 2);
                if (deadlineParts.length < 2) {
                    System.out.println("☹ OOPS!!! The deadline format is wrong. Use: deadline <desc> /by <time>");
                    return;
                }
                newTask = new Deadline(deadlineParts[0].trim(), deadlineParts[1].trim());
                break;

            case "event":
                // expect format: event <desc> /from <start> /to <end>
                String[] eventParts = description.split("/from", 2);
                if (eventParts.length < 2) {
                    System.out.println("☹ OOPS!!! The event format is wrong. Use: event <desc> /from <start> /to <end>");
                    return;
                }
                String desc = eventParts[0].trim();
                String[] fromTo = eventParts[1].split("/to", 2);
                if (fromTo.length < 2) {
                    System.out.println("☹ OOPS!!! The event format is wrong. Use: event <desc> /from <start> /to <end>");
                    return;
                }
                newTask = new Event(desc, fromTo[0].trim(), fromTo[1].trim());
                break;

            default:
                System.out.println("☹ OOPS!!! I don’t know what that means.");
                return;
        }

        // add task into the first free slot
        for (int i = 0; i < input.length; i++) {
            if (input[i] == null) {
                input[i] = newTask;
                System.out.println("____________________________________________________________");
                System.out.println("Got it. I've added this task:");
                System.out.println("  " + newTask);
                System.out.println("Now you have " + (i + 1) + " tasks in the list.");
                System.out.println("____________________________________________________________");
                break;
            }
        }
    }


    public static void listArray(Task[] input) {
        System.out.println("____________________________________________________________");
        System.out.println("Here are the tasks in your list:");
        int i = 0;
        while (i < input.length && input[i] != null) {
            System.out.println((i + 1) + "." + input[i]);
            i += 1;
        }
        System.out.println("____________________________________________________________");
    }


    public static void greetings() {
        System.out.println("____________________________________________________________");
        System.out.println("Hello! I'm OKuke");
        System.out.println("What can I do for you?");
        System.out.println("____________________________________________________________");
    }

    public static void goodbye() {
        System.out.println("____________________________________________________________");
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println("____________________________________________________________");
    }

    public static void printEcho(String message) {
        System.out.println("____________________________________________________________");
        System.out.println("added: " + message);
        System.out.println("____________________________________________________________");
    }

}
