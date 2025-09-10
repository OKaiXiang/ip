package okuke;

import java.util.Objects;

import okuke.command.Command;
import okuke.exception.OkukeException;
import okuke.parser.Parser;
import okuke.storage.Storage;
import okuke.task.TaskList;
import okuke.ui.Ui;

/**
 * Main application entry point and runtime loop for OKuke.
 * Wires Storage, Parser, and Ui, then runs the REPL until exit.
 */
public final class OKuke {

    private static final String MSG_INVALID_INDEX_OR_FORMAT =
            "Invalid index or format. Please check your command.";
    private static final String MSG_UNEXPECTED_ERROR_PREFIX = "[Error] ";

    private final Storage storage;
    private final Ui ui;
    private TaskList tasks;

    /**
     * Constructs the application, initializing storage, UI, and task list.
     * Loads persisted tasks from disk if available.
     */
    public OKuke() {
        this.ui = new Ui();
        this.storage = new Storage(); // your existing self-contained path (./src/data/Okuke.txt)
        this.tasks = loadTasksOrEmpty();
    }

    private TaskList loadTasksOrEmpty() {
        try {
            return new TaskList(storage.load());
        } catch (OkukeException.DataFileMissingException e) {
            // Missing file is non-fatal: show message and continue with empty list.
            ui.showLoadingError(e.getMessage());
        } catch (Exception e) {
            ui.showLoadingError("[okuke.storage.Storage] Failed to load tasks: " + e.getMessage());
        }
        return new TaskList();
    }

    /**
     * Runs the read–evaluate–print loop:
     *  1. Read a line from the UI
     *  2. Parse into a Command
     *  3. Execute the Command
     *  4. Handle and display errors gracefully
     *  5. Repeat until the Command signals exit
     */
    public void run() {
        ui.showWelcome();

        while (true) {
            try {
                final String fullCommand = ui.readCommand();
                if (fullCommand == null || fullCommand.isBlank()) {
                    // Guard clause: ignore blank lines to keep the main flow linear.
                    continue;
                }

                final Command command = Parser.parse(fullCommand);
                command.execute(tasks, ui, storage);

                if (command.isExit()) {
                    break;
                }
            } catch (OkukeException e) {
                ui.showError(e.getMessage());
            } catch (NumberFormatException | IndexOutOfBoundsException e) {
                ui.showError(MSG_INVALID_INDEX_OR_FORMAT);
            } catch (Exception e) {
                ui.showError(MSG_UNEXPECTED_ERROR_PREFIX + safeMessage(e));
            }
        }
    }

    /**
     * Creates and runs the OKuke application.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        new OKuke().run();
    }

    /**
     * Executes one input line through Parser/Command and returns the formatted reply
     * string for the GUI (user bubble on the right, reply on the left).
     *
     * <p>Stateless w.r.t. the UI buffer: builds a short-lived Gui on each call and returns its contents.</p>
     *
     * @param input the raw line the user typed
     * @return the response text to show in the reply bubble
     */
    public String getResponse(String input) {
        assert input != null : "input must not be null";
        final okuke.ui.Gui gui = new okuke.ui.Gui(); // short-lived buffer for one response

        try {
            final Command command = Parser.parse(Objects.requireNonNullElse(input, ""));
            command.execute(tasks, gui, storage);
            if (command.isExit()) {
                gui.showBye();
            }
        } catch (OkukeException e) {
            gui.showError(e.getMessage());
        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            gui.showError(MSG_INVALID_INDEX_OR_FORMAT);
        } catch (Exception e) {
            gui.showError(MSG_UNEXPECTED_ERROR_PREFIX + safeMessage(e));
        }

        return gui.consume();
    }

    // Avoid exposing stack traces to end users, but still show a useful message.
    private static String safeMessage(Throwable t) {
        final String m = t.getMessage();
        return (m == null || m.isBlank()) ? t.getClass().getSimpleName() : m;
    }
}
