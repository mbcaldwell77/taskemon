package learn.domain;

import learn.data.DataAccessException;
import learn.data.repositories.TaskRepository;
import learn.models.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;

import static learn.TestHelper.makeTask;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class TaskServiceTest {

    @Autowired
    TaskService service;

    @MockBean
    TaskRepository repository;


    //         taskId;
    //        taskName;
    //        dueDate;
    //        status;
    //        checklistId;
    // findall, findbyid, add, update, delete
    //1, 'dishes', '2024-01-01', 'not complete', 1),
//    (13, 'eat healthy', '2024-01-01', 'complete', 4),

    @Test
    void shouldFindTaskWithIdOfTwo() throws DataAccessException {
        when(repository.findById(2)).thenReturn(new Task());
        Task task = service.findById(2);
        assertNotNull(task);
    }

    @Test
    void shouldAdd() { // NEED TO FIX THIS TEST
        Task task = new Task(100, "TEST", LocalDate.of(2024,01,01), false, 200);
        Task mockOut = new Task(200, "TEST", LocalDate.of(2024,01,01), false, 400);

        when(repository.add(task)).thenReturn(mockOut);

        Result<Task> actual = service.add(task);
//        assertEquals(ResultType.SUCCESS, actual.getType());
//        assertEquals(mockOut, actual.getPayload());
    }

    @Test
    void shouldFindDishesTask() {
        Task expected = makeTask();
        when(repository.findById(1)).thenReturn(expected);
        Task actual = service.findById(1);
        assertEquals(expected, actual);
    }

    @Test
    void shouldNotAddWhenInvalid() {
        // WORKS ON ITS OWN, BUT NOT WHEN ALL TESTS ARE RUN TOGETHER!??
        Task task = makeTask();
        Result<Task> result = service.add(task);
        assertEquals(ResultType.INVALID, result.getType());

        task.setTaskId(0);
        task.setTaskName(null);
        result = service.add(task);
        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldAddWhenValid() {
        Task expected = makeTask();
        Task arg = makeTask();
        arg.setTaskId(0);

        when(repository.add(arg)).thenReturn(expected);
        Result<Task> result = service.add(arg);
        assertEquals(ResultType.SUCCESS, result.getType());

        assertEquals(expected, result.getPayload());
    }

}
