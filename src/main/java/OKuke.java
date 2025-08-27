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
            printEcho(line);
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
        for (int i = 0; i < input.length; i++) {
            if (input[i] == null) {
                input[i] = new Task(message);
                break;
            }
        }
    }

    public static void listArray(Task[] input) {
        System.out.println("____________________________________________________________");
        System.out.println("Here are the tasks in your list:");
        int i = 0;
        while (input[i] != null) {
            System.out.println(input[i].toString());
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
