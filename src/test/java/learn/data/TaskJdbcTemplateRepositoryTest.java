package learn.data;

import learn.data.jdbctemplaterepositories.TaskJdbcTemplateRepository;
import learn.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;



@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class TaskJdbcTemplateRepositoryTest {

    @Autowired
    TaskJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }

    @Test
    void shouldFindTasks() {
        List<Task> tasks = repository.findAll();
        assertNotNull(tasks);
        assertTrue(tasks.size() >= 1 && tasks.size() <= 20);
        assertFalse(tasks.size() >= 1 && tasks.size() <= 18);

    }

    @Test
    void shouldFindDishes() {
        // not working: error is "Cannot add or update a child row...", which doesn't make sense, since nothing added/updated
        //task id 1, name = dishes, due date 2024-01-01, status not complete, checklistid = 1
        Task task = repository.findById(1);
        assertEquals(1, task.getTaskId());

        assertEquals("dishes", task.getTaskName());

        assertEquals(LocalDate.of(2024,01,01), task.getDueDate());
        assertEquals(false, task.getStatus());
        assertEquals(1, task.getChecklistId());
    }




    @Test
    void shouldAddTask() { // not working: error is "Cannot add or update a child row..."
        Task task = new Task();
        task.setTaskName("TESTaTASK");
        task.setDueDate(LocalDate.of(2024,01,01));
        task.setStatus(true);
        task.setChecklistId(1);
        Task actual = repository.add(task);
        assertNotNull(actual);
        assertEquals(21, actual.getTaskId());
    }

    @Test
    void shouldUpdateTask() {
        Task task = new Task();
        task.setTaskId(1);
        task.setTaskName("TESTaTASK");
        task.setDueDate(LocalDate.of(2024,01,01));
        task.setStatus(true);
        task.setChecklistId(1);
        assertTrue(repository.update(task));
    }

    @Test
    void shouldDeleteTask() { // test working, but repository adjustment might be faulty
        assertTrue(repository.deleteById(3));
        assertFalse(repository.deleteById(3));
    }

}
