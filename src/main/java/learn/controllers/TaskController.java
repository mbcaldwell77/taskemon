package learn.controllers;

import learn.domain.ChecklistService;
import learn.domain.Result;
import learn.domain.TaskService;
import learn.models.Checklist;
import learn.models.Task;
import learn.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5500", "http://127.0.0.1:5500"})
@RequestMapping("/api/task")
public class TaskController {

	private final TaskService taskService;
	private final ChecklistService checklistService;

	public TaskController(TaskService taskService, ChecklistService checklistService) {
		this.taskService = taskService;
		this.checklistService = checklistService;
	}

	@GetMapping
	public List<Task> findAll() {
		return taskService.findAll();
	}

	@GetMapping("/{taskId}")
	public Task findById(@PathVariable int taskId) {
		return taskService.findById(taskId);
	}

	@PostMapping
	public ResponseEntity<Object> add(@RequestBody Task task) {
		Result<Task> result = taskService.add(task);
		if (result.isSuccess()) {
			return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
		}
		return ErrorResponse.build(result);
	}

	@PutMapping("/{taskId}")
	public ResponseEntity<Object> update(@PathVariable int taskId, @RequestBody Task task) {
		if (taskId != task.getTaskId()) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}

		Result<Task> result = taskService.update(task);
		if (result.isSuccess()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		return ErrorResponse.build(result);
	}

//	@DeleteMapping("/{taskId}")
//	public ResponseEntity<Void> deleteById(@PathVariable int taskId) {
//		if (taskService.deleteById(taskId)) {
//			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//		}
//		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//	}

	@DeleteMapping("/{taskId}")
	public ResponseEntity<Object> deleteTaskById(@PathVariable int taskId, @PathVariable int checklistId, @AuthenticationPrincipal User user) {
		List<String> authorities = user.getAuthorities().stream()
				.map(a -> a.getAuthority())
				.toList();

		Checklist list = checklistService.findById(checklistId);
		Task task = taskService.findById(checklistId);
		if (list.getUserId() == user.getUserId() && list.getChecklistId() == task.getChecklistId()) {
			taskService.deleteById(taskId);
		} else {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		return null;
	}

}
