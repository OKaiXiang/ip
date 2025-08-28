import java.util.Scanner;

public class OKuke {
    public static void main(String[] args) {
        Task[] list = new Task[100];
        greetings();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            String line = scanner.nextLine().trim();
            String[] parts = line.split(" ");

            try {
                if (line.equals("bye")) {
                    goodbye();
                    return;
                } else if (line.equals("list")) {
                    listArray(list);
                } else if (parts[0].equals("mark")) {
                    int markedNum = Integer.parseInt(parts[1]);
                    mark(list, markedNum);
                } else if (parts[0].equals("unmark")) {
                    int unmarkedNum = Integer.parseInt(parts[1]);
                    unmark(list, unmarkedNum);
                } else {
                    addArray(list, line);
                }
            } catch (OkukeException e) {
                System.out.println(e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("____________________________________________________________");
                System.out.println("Please enter a valid task number.");
                System.out.println("____________________________________________________________");
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("____________________________________________________________");
                System.out.println("Task number is missing or invalid.");
                System.out.println("____________________________________________________________");
            }
        }
    }

    public static void mark(Task[] list, int input) throws OkukeException {
        if (input < 1 || input > 100 || list[input - 1] == null) {
            throw new OkukeException.InvalidTaskIndexException();
        }
        list[input - 1].setMark();
        System.out.println("____________________________________________________________");
        System.out.println("Nice! I've marked this task as done:");
        System.out.println(list[input - 1]);
        System.out.println("____________________________________________________________");
    }

    public static void unmark(Task[] list, int input) throws OkukeException {
        if (input < 1 || input > 100 || list[input - 1] == null) {
            throw new OkukeException.InvalidTaskIndexException();
        }
        list[input - 1].unMark();
        System.out.println("____________________________________________________________");
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println(list[input - 1]);
        System.out.println("____________________________________________________________");
    }

    public static void addArray(Task[] input, String message) throws OkukeException {
        String[] parts = message.split(" ", 2);
        String command = parts[0];
        String description = (parts.length > 1) ? parts[1] : "";

        Task newTask;

        switch (command) {
            case "todo":
                if (description.isEmpty()) {
                    throw new OkukeException.MissingTaskNameException();
                }
                newTask = new Todo(description);
                break;

            case "deadline":
                if (!description.contains("/by")) {
                    throw new OkukeException.MissingDeadlineArgumentsException();
                }

                String[] deadlineParts = description.split("/by", 2);

                if (deadlineParts[0].trim().isEmpty()) {
                    throw new OkukeException.MissingDeadlineArgumentsException();
                }
                if (deadlineParts[1].trim().isEmpty()) {
                    throw new OkukeException.MissingDateException();
                }
                newTask = new Deadline(deadlineParts[0].trim(), deadlineParts[1].trim());
                break;

            case "event":
                String[] eventParts = description.split("/from", 2);
                if (eventParts.length < 2) {
                    throw new OkukeException.MissingEventArgumentsException();
                }
                String desc = eventParts[0].trim();
                String[] fromTo = eventParts[1].split("/to", 2);
                if (fromTo.length < 2) {
                    throw new OkukeException.MissingEventArgumentsException();
                }
                newTask = new Event(desc, fromTo[0].trim(), fromTo[1].trim());
                break;

            default:
                throw new OkukeException.InvalidCommandException();
        }

        // Add task to the first free slot
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
        for (int i = 0; i < input.length && input[i] != null; i++) {
            System.out.println((i + 1) + "." + input[i]);
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

    public static int findStringIndex(String[] description, String target) {
        for (int i = 0; i < description.length; i++) {
            if (description[i].equals(target)) {
                return i;
            }
        }
        return -1;
    }
}
