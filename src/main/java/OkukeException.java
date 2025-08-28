public class OkukeException extends Exception {

    public OkukeException(String errorMessage) {
        super(errorMessage);
    }

    public static class InvalidCommandException extends OkukeException {
        private static final String errorMessage = """
                ______________________________________________
                Invalid command! Please try again.
                ______________________________________________""";

        public InvalidCommandException() {
            super(errorMessage);
        }
    }

    public static class InvalidTaskIndexException extends OkukeException {
        private static final String errorMessage = """
                ______________________________________________
                Your task cannot be found.
                ______________________________________________""";

        public InvalidTaskIndexException() {
            super(errorMessage);
        }
    }

    public static class MissingTaskNameException extends OkukeException {
        private static final String errorMessage = """
                ______________________________________________
                Your task name cannot be empty.
                ______________________________________________""";

        public MissingTaskNameException() {
            super(errorMessage);
        }
    }

    public static class MissingDateException extends OkukeException {
        private static final String errorMessage = """
                ______________________________________________
                Your date cannot be empty.
                ______________________________________________""";

        public MissingDateException() {
            super(errorMessage);
        }
    }

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
}
