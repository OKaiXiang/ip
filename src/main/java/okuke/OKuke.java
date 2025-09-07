package okuke;

import okuke.command.Command;
import okuke.exception.OkukeException;
import okuke.parser.Parser;
import okuke.storage.Storage;
import okuke.ui.Ui;
import okuke.task.TaskList;

/**
 * Main application entry point and runtime loop for OKuke.
 * Wires Storage, Parser, and Ui, then runs the REPL until exit.
 */
public class OKuke {

    private final Storage storage;
    private TaskList tasks;
    private final Ui ui;

    /**
     * Constructs the application, initializing storage, UI, and task list.
     * Loads persisted tasks from disk if available.
     */
    public OKuke() {
        this.ui = new Ui();
        this.storage = new Storage(); // your existing self-contained path (./src/data/MochiBot.txt)
        try {
            this.tasks = new TaskList(storage.load());
        } catch (OkukeException.DataFileMissingException e) {
            // Stretch-goal: show missing-file message but continue with empty tasks
            ui.showLoadingError(e.getMessage());
            this.tasks = new TaskList();
        } catch (Exception e) {
            ui.showLoadingError("[okuke.storage.Storage] Failed to load tasks: " + e.getMessage());
            this.tasks = new TaskList();
        }
    }

    /**
     * Runs the read–evaluate–print loop:
     * <ol>
     *   <li>Read a line from the UI</li>
     *   <li>Parse into a Command</li>
     *   <li>Execute the Command (mutating storage/tasks/UI as needed)</li>
     *   <li>Handle and display errors gracefully</li>
     *   <li>Repeat until the Command signals exit</li>
     * </ol>
     */
    public void run() {
        ui.showWelcome();
        boolean isExit = false;
        while (!isExit) {
            try {
                String fullCommand = ui.readCommand();
                Command c = Parser.parse(fullCommand);
                c.execute(tasks, ui, storage);
                isExit = c.isExit();
            } catch (OkukeException e) {
                ui.showError(e.getMessage());
            } catch (NumberFormatException | IndexOutOfBoundsException ex) {
                ui.showError("Invalid index or format. Please check your okuke.command.");
            } catch (Exception ex) {
                ui.showError("[Error] " + ex.getMessage());
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
     * @param input the raw line the user typed
     * @return the response text to show in the reply bubble
     */
    public String getResponse(String input) {
        okuke.ui.Gui gui = new okuke.ui.Gui();
        try {
            Command c = Parser.parse(input);
            c.execute(tasks, gui, storage);
            if (c.isExit()) {
                gui.showBye();
            }
        } catch (OkukeException e) {
            gui.showError(e.getMessage());
        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            gui.showError("Invalid index or format. Please check your command.");
        } catch (Exception ex) {
            gui.showError("[Error] " + ex.getMessage());
        }
        return gui.consume();
    }

}
