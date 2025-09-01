public class Parser {

    public static Command parse(String fullCommand) throws OkukeException {
        if (fullCommand == null || fullCommand.isBlank()) {
            throw new OkukeException.InvalidCommandException();
        }

        String[] parts = fullCommand.split("\\s+", 2);
        String cmd = parts[0];

        switch (cmd) {
            case "bye":
                return new ExitCommand();

            case "list":
                return new ListCommand();

            case "mark":
                if (parts.length < 2) throw new OkukeException.InvalidCommandException();
                return new MarkCommand(parseIndex(parts[1]));

            case "unmark":
                if (parts.length < 2) throw new OkukeException.InvalidCommandException();
                return new UnmarkCommand(parseIndex(parts[1]));

            case "delete":
                if (parts.length < 2) throw new OkukeException.InvalidCommandException();
                return new DeleteCommand(parseIndex(parts[1]));

            case "todo":
                if (parts.length < 2 || parts[1].isBlank()) throw new OkukeException.InvalidCommandException();
                return new AddTodoCommand(parts[1].trim());

            case "deadline": {
                if (parts.length < 2) throw new OkukeException.InvalidCommandException();
                String[] segs = parts[1].split("\\s*/by\\s*", 2);
                if (segs.length != 2) throw new OkukeException.InvalidCommandException();
                return new AddDeadlineCommand(segs[0].trim(), segs[1].trim());
            }

            case "event": {
                if (parts.length < 2) throw new OkukeException.InvalidCommandException();
                String rest = parts[1];
                int fromIdx = rest.indexOf("/from");
                int toIdx = rest.indexOf("/to");
                if (fromIdx < 0 || toIdx < 0 || toIdx <= fromIdx) {
                    throw new OkukeException.MissingEventArgumentsException();
                }
                String desc = rest.substring(0, fromIdx).trim();
                String from = rest.substring(fromIdx + 5, toIdx).trim();
                String to   = rest.substring(toIdx + 3).trim();
                if (desc.isEmpty() || from.isEmpty() || to.isEmpty()) {
                    throw new OkukeException.MissingEventArgumentsException();
                }
                return new AddEventCommand(desc, from, to);
            }

            // Stretch: list items on a date
            case "on":
                if (parts.length < 2 || parts[1].isBlank()) throw new OkukeException.InvalidCommandException();
                return new OnDateCommand(parts[1].trim());

            default:
                throw new OkukeException.InvalidCommandException();
        }
    }

    private static int parseIndex(String s) throws OkukeException.InvalidCommandException {
        try {
            return Integer.parseInt(s.trim());
        } catch (NumberFormatException nfe) {
            throw new OkukeException.InvalidCommandException();
        }
    }
}
