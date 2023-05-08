package learn.domain;

import learn.data.repositories.ChecklistRepository;
import learn.data.repositories.TaskRepository;
import learn.models.Task;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TaskService {

	private final TaskRepository repository;
	private final ChecklistRepository checklistRepository;

	public TaskService(TaskRepository repository, ChecklistRepository checklistRepository) {
		this.repository = repository;
		this.checklistRepository = checklistRepository;
	}

	public List<Task> findAll() {
		return repository.findAll();
	}

	public Task findById(int taskId) {
		return repository.findById(taskId);
	}

	public Result<Task> add(Task task) {
		Result<Task> result = validate(task);
		if (!result.isSuccess()) {
			return result;
		}

		if (task.getTaskId() != 0) {
			result.addMessage("taskId cannot be set for `add` operation", ResultType.INVALID);
			return result;
		}

		task = repository.add(task);
		result.setPayload(task);
		return result;
	}

	public Result<Task> update(Task task) {
		Result<Task> result = validate(task);
		if (!result.isSuccess()) {
			return result;
		}

		if (task.getTaskId() <= 0) {
			result.addMessage("taskId must be set for `update` operation", ResultType.INVALID);
			return result;
		}

		if (!repository.update(task)) {
			String msg = String.format("taskId: %s, not found", task.getTaskId());
			result.addMessage(msg, ResultType.NOT_FOUND);
		}

		return result;
	}

	public boolean deleteById(int taskId) {
		return repository.deleteById(taskId);
	}

	private Result<Task> validate(Task task) {
		Result<Task> result = new Result<>();
		if (task == null) {
			result.addMessage("task cannot be null", ResultType.INVALID);
			return result;
		}

		if (Validations.isNullOrBlank(task.getTaskName())) {
			result.addMessage("a task name is required", ResultType.INVALID);
		}

		//TODO double check this is correct
		if (task.getDueDate() != null && task.getDueDate().isBefore(LocalDate.now())) {
			result.addMessage("must provide a future due date", ResultType.INVALID);
		}

		if (task.getChecklistId() == 0) {
			result.addMessage("checklist Id is required", ResultType.INVALID);
		} else if (checklistRepository.findById(task.getChecklistId()) == null) {
			result.addMessage("checklist must exist", ResultType.INVALID);
		}

		return result;
	}
}

