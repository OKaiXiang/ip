import java.util.Scanner;

public class OKuke {
    public static void main(String[] args) {
        greetings();
        while(true) {
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            if(input.equals("bye")) {
                goodbye();
                return;
            }
            printEcho(input);
        }
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
        System.out.println(message);
        System.out.println("____________________________________________________________");
    }

}
