package okuke.command;

import okuke.exception.OkukeException;
import okuke.storage.Storage;
import okuke.task.TaskList;
import okuke.ui.Ui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

public class AddEventCommandTest {
    private TaskList tasks;
    private Ui ui;
    private Storage storage;

    private PrintStream originalOut;
    private ByteArrayOutputStream out;

    @BeforeEach
    void setup() {
        tasks = new TaskList();
        ui = new Ui();
        storage = mock(Storage.class);

        originalOut = System.out;
        out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    public void execute_validInput_printsAddedMessage() throws OkukeException {
        AddEventCommand c = new AddEventCommand(
                "project meeting",
                "2025-09-10T14:00",
                "2025-09-10T16:00"
        );

        c.execute(tasks, ui, storage);

        String printed = out.toString().toLowerCase();
        assertTrue(printed.contains("added: project meeting"),
                "Expected UI to print an 'added' message with the description.");
        assertTrue(printed.contains("now you have"),
                "Expected UI to show the updated task count.");
    }

    @Test
    public void execute_invalidFrom_throws() {
        AddEventCommand c = new AddEventCommand(
                "x",
                "not-a-date",
                "2025-09-10T16:00"
        );

        assertThrows(OkukeException.InvalidCommandException.class,
                () -> c.execute(tasks, ui, storage));
    }

    @Test
    public void execute_invalidTo_throws() {
        AddEventCommand c = new AddEventCommand(
                "x",
                "2025-09-10T14:00",
                "also-not-a-date"
        );

        assertThrows(OkukeException.InvalidCommandException.class,
                () -> c.execute(tasks, ui, storage));
    }
}
