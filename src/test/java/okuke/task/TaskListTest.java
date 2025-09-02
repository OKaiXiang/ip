package okuke.task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TaskListTest {

    private TaskList tasks;

    @BeforeEach
    void setup() {
        tasks = new TaskList();
    }

    @Test
    public void constructor_empty_hasSizeZero() {
        assertEquals(0, tasks.size());
    }

    @Test
    public void add_and_get_returnsSameInstance() {
        Todo todo = new Todo("eat bread");
        tasks.add(todo);

        assertEquals(1, tasks.size());
        assertSame(todo, tasks.get(0));
    }

    @Test
    public void removeOneBased_removesCorrectElement() {
        Todo first = new Todo("first");
        Todo second = new Todo("second");
        tasks.add(first);
        tasks.add(second);

        Task removed = tasks.removeOneBased(1); // 1-based index
        assertSame(first, removed);
        assertEquals(1, tasks.size());
        assertSame(second, tasks.get(0));
    }

    @Test
    public void removeOneBased_outOfRange_throws() {
        assertThrows(IndexOutOfBoundsException.class, () -> tasks.removeOneBased(1));
    }

    @Test
    public void markAndUnmarkOneBased_togglesRepresentation() {
        Todo todo = new Todo("toggle me");
        tasks.add(todo);

        String before = todo.toString();
        Task marked = tasks.markOneBased(1);
        String afterMark = todo.toString();
        Task unmarked = tasks.unmarkOneBased(1);
        String afterUnmark = todo.toString();

        // Sanity: the same instance is returned
        assertSame(todo, marked);
        assertSame(todo, unmarked);

        // We don't have isMarked(), so rely on toString() changing
        assertNotEquals(before, afterMark, "Marking should change string representation.");
        assertNotEquals(afterMark, afterUnmark, "Unmarking should change from marked representation.");
        // Optionally check round-trip (uncomment if your toString returns to original form)
        // assertEquals(before, afterUnmark);
    }

    @Test
    public void occurringOn_includesDeadlineOnThatDate() {
        LocalDateTime by = LocalDateTime.of(2025, 9, 10, 23, 59);
        Deadline dl = new Deadline("submit report", by);
        tasks.add(dl);

        List<Task> sameDay = tasks.occurringOn(LocalDate.of(2025, 9, 10));
        assertEquals(1, sameDay.size());
        assertSame(dl, sameDay.get(0));

        List<Task> otherDay = tasks.occurringOn(LocalDate.of(2025, 9, 9));
        assertTrue(otherDay.isEmpty());
    }

    @Test
    public void occurringOn_includesEventWhenDateWithinRange_inclusive() {
        LocalDateTime start = LocalDateTime.of(2025, 9, 10, 14, 0);
        LocalDateTime end   = LocalDateTime.of(2025, 9, 12, 16, 0);
        Event ev = new Event("project meeting", start, end);
        tasks.add(ev);

        // inside range
        assertTrue(tasks.occurringOn(LocalDate.of(2025, 9, 11)).contains(ev));

        // inclusive on start and end
        assertTrue(tasks.occurringOn(LocalDate.of(2025, 9, 10)).contains(ev));
        assertTrue(tasks.occurringOn(LocalDate.of(2025, 9, 12)).contains(ev));

        // outside range
        assertFalse(tasks.occurringOn(LocalDate.of(2025, 9, 9)).contains(ev));
        assertFalse(tasks.occurringOn(LocalDate.of(2025, 9, 13)).contains(ev));
    }
}
