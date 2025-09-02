package okuke.exception;

/**
 * Base checked exception for all user and I/O errors in OKuke.
 * Subclasses provide specific error messages for common failure modes.
 */
public class OkukeException extends Exception {

    /**
     * Creates a new OKuke exception with a formatted error message
     * suitable for direct display in the UI.
     *
     * @param errorMessage human-readable description of the error
     */
    public OkukeException(String errorMessage) {
        super(errorMessage);
    }

    /**
     * Thrown when the user enters a command that does not match any
     * supported command syntax.
     */
    public static class InvalidCommandException extends OkukeException {
        private static final String errorMessage = """
                ______________________________________________
                Invalid command! Please try again.
                ______________________________________________""";

        public InvalidCommandException() {
            super(errorMessage);
        }
    }

    /**
     * Thrown when a task index provided by the user does not exist
     * (e.g., out of bounds or not a valid integer).
     */
    public static class InvalidTaskIndexException extends OkukeException {
        private static final String errorMessage = """
                ______________________________________________
                Your task cannot be found.
                ______________________________________________""";

        public InvalidTaskIndexException() {
            super(errorMessage);
        }
    }

    /**
     * Thrown when a command that requires a task name (e.g., todo)
     * is missing the description portion.
     */
    public static class MissingTaskNameException extends OkukeException {
        private static final String errorMessage = """
                ______________________________________________
                Your task name cannot be empty.
                ______________________________________________""";

        public MissingTaskNameException() {
            super(errorMessage);
        }
    }

    /**
     * Thrown when a command that requires a date or date-time argument
     * omits it or provides it in the wrong position.
     */
    public static class MissingDateException extends OkukeException {
        private static final String errorMessage = """
                ______________________________________________
                Your date cannot be empty.
                ______________________________________________""";

        public MissingDateException() {
            super(errorMessage);
        }
    }

    /**
     * Thrown when a "deadline" command is missing required parts or
     * is not in the expected format (e.g., missing "/by <date-time>").
     */
    public static class MissingDeadlineArgumentsException extends OkukeException {
        private static final String errorMessage = """
                ______________________________________________
                Incorrect format detected. Format is:
                \ndeadline <name> /by <date-time>
                ______________________________________________""";

        public MissingDeadlineArgumentsException() {
            super(errorMessage);
        }
    }

    /**
     * Thrown when an "event" command is missing required parts or
     * is not in the expected format (e.g., missing "/from ... /to ...").
     */
    public static class MissingEventArgumentsException extends OkukeException {
        private static final String errorMessage = """
                ______________________________________________
                Incorrect format detected. Format is:
                \nevents <name> /from <date-time> /to <date-time>
                ______________________________________________""";

        public MissingEventArgumentsException() {
            super(errorMessage);
        }
    }

    /**
     * Thrown when the persistent data file is missing at startup.
     * Callers may use the provided path in a follow-up message to the user.
     */
    public static class DataFileMissingException extends OkukeException {
        public DataFileMissingException(String path) {
            super("Data file did not exist. A new one has been created at: " + path);
        }
    }
}
