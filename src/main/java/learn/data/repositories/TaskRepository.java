package learn.data.repositories;

import learn.models.Task;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TaskRepository {

    List<Task> findAll();

    @Transactional
    Task findById(int taskId);

    Task add(Task task);

    boolean update(Task task);

    @Transactional
    boolean deleteById(int taskId);
}
