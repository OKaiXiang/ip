package okuke.storage;

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

import okuke.exception.OkukeException;
import okuke.task.Deadline;
import okuke.task.Event;
import okuke.task.Task;
import okuke.task.Todo;

/**
 * Handles persistence of tasks to and from a UTF-8 text file on disk.
 * Provides robust loading (skips/flags corrupted lines) and lossless saving
 * using a simple, line-oriented format.
 */
public class Storage {
    private static final String FILE_PATH = "./src/data/OKuke.txt";
    private static final String SEP = " | ";

    private static final DateTimeFormatter ISO_DT = DateTimeFormatter.ISO_LOCAL_DATE_TIME; // yyyy-MM-ddTHH:mm:ss[.SSS]
    private static final DateTimeFormatter ISO_D  = DateTimeFormatter.ISO_LOCAL_DATE;      // yyyy-MM-dd

    private final Path path;

    /**
     * Constructs a new {@code Storage} bound to the configured file path.
     * Ensures parent directories exist and prepares for load/save operations.
     */
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
     * Loads tasks from disk into memory.
     * <ul>
     *   <li>Creates the data file if the parent directory is present but the file is missing.</li>
     *   <li>Parses each line into {@link okuke.task.Task} subtypes (Todo/Deadline/Event).</li>
     *   <li>Silently skips lines that are obviously corrupted/unparseable.</li>
     * </ul>
     *
     * @return a mutable {@code List<Task>} representing all loaded tasks
     * @throws java.io.IOException if the file cannot be read
     * @throws okuke.exception.OkukeException.DataFileMissingException if the data file path does not exist
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

    /**
     * Persists the provided tasks to disk, overwriting the existing file.
     * Uses a stable, line-based format compatible with {@link #load()}.
     *
     * @param tasks the tasks to serialize
     * @throws java.io.IOException if the file cannot be written
     */
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

    /**
     * Converts a single task into its on-disk line representation.
     * Includes type tag, done flag, description, and any date/time fields.
     *
     * @param t the task to encode
     * @return serialized single-line form of the task
     */
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

    /**
     * Parses one serialized line from the data file into a {@link okuke.task.Task}.
     * Recognizes Todo, Deadline (with "/by"), and Event (with "/from" and "/to").
     * Returns {@code null} for clearly corrupted lines so callers can skip them.
     *
     * @param line one line of task data (without line terminator)
     * @return the parsed task, or {@code null} if the line is invalid
     */
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

    /**
     * Parses a date string in ISO date-time or ISO date form.
     * If only a date is provided, returns the start of day (00:00).
     *
     * @param s the date/time string to parse
     * @return parsed {@code LocalDateTime}
     */
    private static LocalDateTime parseIsoDateOrDateTime(String s) {
        String in = s.trim();
        try { return LocalDateTime.parse(in, ISO_DT); } catch (Exception ignore) {}
        // Fallback: just a date -> start of day
        return LocalDate.parse(in, ISO_D).atStartOfDay();
    }
}
