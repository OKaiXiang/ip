package okuke;

import okuke.command.Command;
import okuke.exception.OkukeException;
import okuke.parser.Parser;
import okuke.storage.Storage;
import okuke.ui.Ui;
import okuke.task.TaskList;

public class OKuke {

    private final Storage storage;
    private TaskList tasks;
    private final Ui ui;

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

    public static void main(String[] args) {
        new OKuke().run();
    }
}
