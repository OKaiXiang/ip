package okuke.storage;

import okuke.exception.OkukeException;
import okuke.task.Deadline;
import okuke.task.Event;
import okuke.task.Task;
import okuke.task.Todo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Storage {
    private static final String FILE_PATH = "./src/data/OKuke.txt";
    private static final String SEP = " | ";

    private static final DateTimeFormatter ISO_DT = DateTimeFormatter.ISO_LOCAL_DATE_TIME; // yyyy-MM-ddTHH:mm:ss[.SSS]
    private static final DateTimeFormatter ISO_D  = DateTimeFormatter.ISO_LOCAL_DATE;      // yyyy-MM-dd

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

    /** Loads all tasks from disk; creates file if missing and throws the stretch-goal okuke.exception for UI. */
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
                String trimmed = line.trim();
                if (trimmed.isEmpty()) continue;

                Task t = parseLine(trimmed);
                if (t != null) {
                    tasks.add(t);
                } else {
                    System.err.println("[okuke.storage.Storage] Skipped corrupted line " + lineno + ": " + trimmed);
                }
            }
        }
        return tasks;
    }

    /** Saves the entire okuke.task list (overwrite). */
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

    // ---------- encoding/decoding (self-contained) ----------

    private String formatLine(Task t) {
        String done = "X".equals(t.getStatus()) ? "1" : "0";
        if (t instanceof Todo) {
            return "T" + SEP + done + SEP + t.getTaskName();
        } else if (t instanceof Deadline) {
            Deadline d = (Deadline) t;
            return "D" + SEP + done + SEP + d.getTaskName() + SEP + d.getByDateTime().format(ISO_DT);
        } else if (t instanceof Event) {
            Event e = (Event) t;
            return "E" + SEP + done + SEP + e.getTaskName()
                    + SEP + e.getStartDateTime().format(ISO_DT)
                    + SEP + e.getEndDateTime().format(ISO_DT);
        }
        return null; // unknown type
    }

    private Task parseLine(String line) {
        try {
            String[] parts = line.split("\\s\\|\\s"); // exact " | "
            if (parts.length < 3) return null;

            String type = parts[0];
            boolean done = "1".equals(parts[1]);

            switch (type) {
                case "T": {
                    if (parts.length != 3) return null;
                    Todo t = new Todo(parts[2]);
                    if (done) t.setMark();
                    return t;
                }
                case "D": {
                    if (parts.length != 4) return null;
                    LocalDateTime by = parseIsoDateOrDateTime(parts[3]);
                    Deadline d = new Deadline(parts[2], by);
                    if (done) d.setMark();
                    return d;
                }
                case "E": {
                    if (parts.length != 5) return null;
                    LocalDateTime from = parseIsoDateOrDateTime(parts[3]);
                    LocalDateTime to   = parseIsoDateOrDateTime(parts[4]);
                    Event e = new Event(parts[2], from, to);
                    if (done) e.setMark();
                    return e;
                }
                default:
                    return null;
            }
        } catch (Exception ex) {
            return null; // treat as corrupted
        }
    }

    /** Accepts ISO date-time or date (for robustness with older files). */
    private static LocalDateTime parseIsoDateOrDateTime(String s) {
        String in = s.trim();
        try { return LocalDateTime.parse(in, ISO_DT); } catch (Exception ignore) {}
        // Fallback: just a date -> start of day
        return LocalDate.parse(in, ISO_D).atStartOfDay();
    }
}
