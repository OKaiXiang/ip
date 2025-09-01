import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OKuke {

    private static void greetings() {
        System.out.println("Hello! I'm OKuke.\nWhat can I do for you?");
    }

    private static void goodbye() {
        System.out.println("Bye. Hope to see you again soon!");
    }

    private static void listTasks(List<Task> list) {
        System.out.println("____________________________________________________________");
        if (list.isEmpty()) {
            System.out.println("Your list is empty.");
        } else {
            System.out.println("Here are the tasks in your list:");
            for (int i = 0; i < list.size(); i++) {
                System.out.printf("%d.%s%n", i + 1, list.get(i).toString());
            }
        }
        System.out.println("____________________________________________________________");
    }

    private static void saveOrWarn(Storage storage, List<Task> list) {
        try {
            storage.save(list);
        } catch (Exception e) {
            System.err.println("[Storage] Failed to save: " + e.getMessage());
        }
    }

    private static void addTodo(List<Task> list, String desc) {
        list.add(new Todo(desc));
        System.out.println("____________________________________________________________");
        System.out.println("added: " + desc);
        System.out.println("____________________________________________________________");
    }

    private static void addDeadline(List<Task> list, String desc, String by) {
        list.add(new Deadline(desc, by));
        System.out.println("____________________________________________________________");
        System.out.println("added: " + desc + " (by: " + by + ")");
        System.out.println("____________________________________________________________");
    }

    private static void addEvent(List<Task> list, String desc, String from, String to) {
        list.add(new Event(desc, from, to));
        System.out.println("____________________________________________________________");
        System.out.println("added: " + desc + " (from: " + from + " to: " + to + ")");
        System.out.println("____________________________________________________________");
    }

    private static void mark(List<Task> list, int idx1) {
        Task t = list.get(idx1 - 1);
        t.setMark();
        System.out.println("____________________________________________________________");
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("____________________________________________________________");
        System.out.println("  " + t);
    }

    private static void unmark(List<Task> list, int idx1) {
        Task t = list.get(idx1 - 1);
        t.unMark();
        System.out.println("____________________________________________________________");
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println("____________________________________________________________");
        System.out.println("  " + t);
    }

    private static void deleteTask(List<Task> list, int idx1) {
        Task removed = list.remove(idx1 - 1);
        System.out.println("____________________________________________________________");
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + removed);
        System.out.println("Now you have " + list.size() + " tasks in the list.");
        System.out.println("____________________________________________________________");
    }

    public static void main(String[] args) {
        Storage storage = new Storage();
        List<Task> list;

        // Load on startup; show stretch-goal message if file didn't exist previously
        try {
            list = storage.load();
        } catch (OkukeException.DataFileMissingException e) {
            System.out.println(e.getMessage()); // "file does not exist ... created ..."
            list = new ArrayList<>();
        } catch (Exception e) {
            System.err.println("[Storage] Failed to load tasks: " + e.getMessage());
            list = new ArrayList<>();
        }

        greetings();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;

            try {
                if (line.equals("bye")) {
                    goodbye();
                    return;
                }

                if (line.equals("list")) {
                    listTasks(list);
                    continue;
                }

                String[] parts = line.split("\\s+", 2);
                String cmd = parts[0];

                switch (cmd) {
                    case "mark": {
                        int idx = Integer.parseInt(parts[1]);
                        mark(list, idx);
                        saveOrWarn(storage, list);
                        break;
                    }
                    case "unmark": {
                        int idx = Integer.parseInt(parts[1]);
                        unmark(list, idx);
                        saveOrWarn(storage, list);
                        break;
                    }
                    case "delete": {
                        int idx = Integer.parseInt(parts[1]);
                        deleteTask(list, idx);
                        saveOrWarn(storage, list);
                        break;
                    }
                    case "todo": {
                        if (parts.length < 2 || parts[1].isBlank()) {
                            throw new OkukeException.InvalidCommandException();
                        }
                        addTodo(list, parts[1].trim());
                        saveOrWarn(storage, list);
                        break;
                    }
                    case "deadline": {
                        // deadline <desc> /by <when>
                        if (parts.length < 2) throw new OkukeException.InvalidCommandException();
                        String[] segs = parts[1].split("\\s*/by\\s*", 2);
                        if (segs.length != 2) throw new OkukeException.InvalidCommandException();
                        addDeadline(list, segs[0].trim(), segs[1].trim());
                        saveOrWarn(storage, list);
                        break;
                    }
                    case "event": {
                        // event <desc> /from <start> /to <end>
                        if (parts.length < 2) throw new OkukeException.InvalidCommandException();
                        String rest = parts[1];
                        int fromIdx = rest.indexOf("/from");
                        int toIdx = rest.indexOf("/to");
                        if (fromIdx < 0 || toIdx < 0 || toIdx <= fromIdx) {
                            throw new OkukeException.MissingEventArgumentsException();
                        }
                        String desc = rest.substring(0, fromIdx).trim();
                        String from = rest.substring(fromIdx + 5, toIdx).trim();
                        String to = rest.substring(toIdx + 3).trim();
                        if (desc.isEmpty() || from.isEmpty() || to.isEmpty()) {
                            throw new OkukeException.MissingEventArgumentsException();
                        }
                        addEvent(list, desc, from, to);
                        saveOrWarn(storage, list);
                        break;
                    }
                    default:
                        throw new OkukeException.InvalidCommandException();
                }
            } catch (OkukeException.InvalidCommandException ice) {
                System.out.println(ice.getMessage());
            } catch (OkukeException.MissingEventArgumentsException me) {
                System.out.println(me.getMessage());
            } catch (NumberFormatException | IndexOutOfBoundsException ex) {
                System.out.println("____________________________________________________________");
                System.out.println("Invalid index or format. Please check your command.");
                System.out.println("____________________________________________________________");
            } catch (Exception ex) {
                System.err.println("[Error] " + ex.getMessage());
            }
        }
    }
}
