import java.util.Scanner;

public class OKuke {
    public static void main(String[] args) {
        String[] list = new String[100];
        greetings();
        while(true) {
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            if(input.equals("bye")) {
                goodbye();
                return;
            }
            if(input.equals("list")) {
                listArray(list);
                continue;
            }
            addArray(list, input);
            printEcho(input);
        }
    }

    public static void addArray(String[] input, String message) {
        for (int i = 0; i < input.length; i++) {
            if (input[i] == null) {
                input[i] = message;
                break;
            }
        }
        //return input;
    }

    public static void listArray(String[] input) {
        System.out.println("____________________________________________________________");
        int i = 0;
        while (input[i] != null) {
            System.out.println(i + 1 + ". " + input[i]);
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
