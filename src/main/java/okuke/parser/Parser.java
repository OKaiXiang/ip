package okuke.parser;

import java.util.Objects;
import java.util.regex.Pattern;

import okuke.exception.OkukeException;
import okuke.command.Command;
import okuke.command.ExitCommand;
import okuke.command.ListCommand;
import okuke.command.MarkCommand;
import okuke.command.UnmarkCommand;
import okuke.command.DeleteCommand;
import okuke.command.AddTodoCommand;
import okuke.command.AddDeadlineCommand;
import okuke.command.AddEventCommand;
import okuke.command.OnDateCommand;
import okuke.command.FindCommand;

/**
 * Parses raw user input strings into executable {@code Command} instances.
 * Supports core commands such as: {@code bye}, {@code list}, {@code mark},
 * {@code unmark}, {@code delete}, {@code todo}, {@code deadline},
 * {@code event}, and date filters (e.g., {@code on <yyyy-MM-dd>}).
 *
 * <p>Validation errors are surfaced via {@link okuke.exception.OkukeException}
 * subclasses with user-friendly messages.</p>
 */
public final class Parser {

    private Parser() { /* utility */ }

    /** Command words as name constants to remove magic strings. */
    private static final class Cmd {
        static final String BYE      = "bye";
        static final String LIST     = "list";
        static final String MARK     = "mark";
        static final String UNMARK   = "unmark";
        static final String DELETE   = "delete";
        static final String TODO     = "todo";
        static final String DEADLINE = "deadline";
        static final String EVENT    = "event";
        static final String ON       = "on";
        static final String FIND     = "find";
    }

    // -------- Regex patterns (precompiled for clarity/perf) --------
    private static final Pattern SPACE_SPLIT          = Pattern.compile("\\s+");
    private static final Pattern DEADLINE_BY_SPLIT    = Pattern.compile("\\s+/by\\s+");
    private static final Pattern EVENT_FROM_SPLIT     = Pattern.compile("\\s+/from\\s+");
    private static final Pattern EVENT_TO_SPLIT       = Pattern.compile("\\s+/to\\s+");

    /**
     * Parses a full command line into a concrete {@link okuke.command.Command}.
     *
     * @param line the line the user typed (may include extra spaces)
     * @return a ready-to-execute {@code Command}
     * @throws okuke.exception.OkukeException if the command is unknown or arguments are invalid
     */
    public static Command parse(String line) throws OkukeException {
        Objects.requireNonNull(line, "input cannot be null");
        final String trimmed = line.trim();
        if (trimmed.isEmpty()) {
            throw new OkukeException.InvalidCommandException();
        }

        final String[] parts = SPACE_SPLIT.split(trimmed, 2);
        final String head = parts[0].toLowerCase();
        final String tail = (parts.length > 1) ? parts[1] : "";

        return switch (head) {
            case Cmd.BYE      -> new ExitCommand();
            case Cmd.LIST     -> new ListCommand();
            case Cmd.MARK     -> parseMark(tail);
            case Cmd.UNMARK   -> parseUnmark(tail);
            case Cmd.DELETE   -> parseDelete(tail);
            case Cmd.TODO     -> parseTodo(tail);
            case Cmd.DEADLINE -> parseDeadline(tail);
            case Cmd.EVENT    -> parseEvent(tail);
            case Cmd.ON       -> parseOnDate(tail);
            case Cmd.FIND     -> parseFind(tail);
            default           -> throw new OkukeException.InvalidCommandException();
        };
    }

    private static Command parseMark(String tail) throws OkukeException {
        return new MarkCommand(parseIndex(tail));
    }

    private static Command parseUnmark(String tail) throws OkukeException {
        return new UnmarkCommand(parseIndex(tail));
    }

    private static Command parseDelete(String tail) throws OkukeException {
        return new DeleteCommand(parseIndex(tail));
    }

    private static Command parseTodo(String tail) throws OkukeException {
        if (tail == null || tail.isBlank()) {
            throw new OkukeException.InvalidCommandException();
        }
        return new AddTodoCommand(tail.trim());
    }

    private static Command parseDeadline(String tail) throws OkukeException {
        // "deadline DESC /by WHEN"
        if (tail == null) {
            throw new OkukeException.InvalidCommandException();
        }
        String[] parts = DEADLINE_BY_SPLIT.split(tail, 2);
        if (parts.length != 2 || parts[0].isBlank() || parts[1].isBlank()) {
            throw new OkukeException.InvalidCommandException();
        }
        String description = parts[0].trim();
        String by = parts[1].trim();
        return new AddDeadlineCommand(description, by);
    }

    private static Command parseEvent(String tail) throws OkukeException {
        // "event DESC /from X /to Y"
        if (tail == null) {
            throw new OkukeException.InvalidCommandException();
        }
        String[] first = EVENT_FROM_SPLIT.split(tail, 2);
        if (first.length != 2) {
            throw new OkukeException.InvalidCommandException();
        }
        String description = first[0].trim();
        String[] second = EVENT_TO_SPLIT.split(first[1], 2);
        if (second.length != 2) {
            throw new OkukeException.InvalidCommandException();
        }
        String from = second[0].trim();
        String to   = second[1].trim();
        if (description.isBlank() || from.isBlank() || to.isBlank()) {
            throw new OkukeException.InvalidCommandException();
        }
        return new AddEventCommand(description, from, to);
    }

    private static Command parseOnDate(String tail) throws OkukeException {
        if (tail == null || tail.isBlank()) {
            throw new OkukeException.InvalidCommandException();
        }
        return new OnDateCommand(tail.trim());
    }

    private static Command parseFind(String tail) throws OkukeException {
        if (tail == null || tail.isBlank()) {
            throw new OkukeException.InvalidCommandException();
        }
        return new FindCommand(tail.trim());
    }

    /**
     * Converts a 1-based index string (as typed by the user) into an integer.
     * Rejects non-numeric inputs and surfaces a friendly parse error.
     *
     * @param s the user-provided index token
     * @return the parsed integer index
     * @throws okuke.exception.OkukeException.InvalidCommandException if {@code s} is not a valid integer
     */
    private static int parseIndex(String s) throws OkukeException.InvalidCommandException {
        try {
            int idx = Integer.parseInt(s.trim());
            if (idx <= 0) throw new NumberFormatException("index must be positive (1-based)");
            return idx;
        } catch (Exception nfe) {
            throw new OkukeException.InvalidCommandException();
        }
    }
}
