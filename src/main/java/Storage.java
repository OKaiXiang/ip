import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Storage {
    // Use a relative, OS-independent path; change name if you prefer.
    private static final String FILE_PATH = "./src/data/OKuke.txt";
    private static final String SEP = " | ";

    private final Path path;

    public Storage() {
        this.path = Paths.get(FILE_PATH).normalize();
    }

    private void ensureExists() throws IOException {
        Path parent = path.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        if (!Files.exists(path)) {
            Files.createFile(path);
        }
    }

    /**
     * Loads all tasks from disk.
     * Stretch-goal behavior: on first run, creates the file and throws
     * OkukeException.DataFileMissingException so caller can show a friendly message.
     */
    public List<Task> load() throws IOException, OkukeException.DataFileMissingException {
        List<Task> tasks = new ArrayList<>();

        if (!Files.exists(path)) {
            ensureExists();
            throw new OkukeException.DataFileMissingException(path.toString());
        }

        try (BufferedReader br = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line;
            int lineno = 0;
            while ((line = br.readLine()) != null) {
                lineno++;
                line = line.trim();
                if (line.isEmpty()) continue;

                Task t = parseLine(line);
                if (t != null) {
                    tasks.add(t);
                } else {
                    System.err.println("[Storage] Skipped corrupted line " + lineno + ": " + line);
                }
            }
        }
        return tasks;
    }

    /** Saves the entire task list (overwrite). */
    public void save(List<Task> tasks) throws IOException {
        ensureExists();
        try (BufferedWriter bw = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            for (Task t : tasks) {
                String encoded = formatLine(t);
                if (encoded != null) {
                    bw.write(encoded);
                    bw.newLine();
                }
            }
        }
    }

    // ---------------- helpers ----------------

    // We rely on Task#getStatus() which returns "X" or " ".
    private static boolean isMarked(Task t) {
        return "X".equals(t.getStatus());
    }

    private String formatLine(Task t) {
        String done = isMarked(t) ? "1" : "0";

        if (t instanceof Todo) {
            // T | done | desc
            return "T" + SEP + done + SEP + t.getTaskName();
        } else if (t instanceof Deadline) {
            Deadline d = (Deadline) t;
            // D | done | desc | by
            return "D" + SEP + done + SEP + d.getTaskName() + SEP + d.getDeadline();
        } else if (t instanceof Event) {
            Event e = (Event) t;
            // E | done | desc | from | to
            return "E" + SEP + done + SEP + e.getTaskName() + SEP + e.getStartDate() + SEP + e.getEndDate();
        }
        // Unknown type — skip
        return null;
    }

    private Task parseLine(String line) {
        try {
            String[] parts = line.split("\\s\\|\\s"); // split by " | "
            if (parts.length < 3) return null;

            String type = parts[0];
            boolean done = "1".equals(parts[1]);

            switch (type) {
                case "T": {
                    // T | done | desc
                    if (parts.length != 3) return null;
                    Todo t = new Todo(parts[2]);
                    if (done) t.setMark();
                    return t;
                }
                case "D": {
                    // D | done | desc | by
                    if (parts.length != 4) return null;
                    Deadline d = new Deadline(parts[2], parts[3]);
                    if (done) d.setMark();
                    return d;
                }
                case "E": {
                    // E | done | desc | from | to
                    if (parts.length != 5) return null;
                    Event e = new Event(parts[2], parts[3], parts[4]);
                    if (done) e.setMark();
                    return e;
                }
                default:
                    return null;
            }
        } catch (Exception ex) {
            return null; // corrupted line -> skip
        }
    }
}
