package learn.data.jdbctemplaterepositories;

import learn.data.mappers.TaskMapper;
import learn.data.repositories.TaskRepository;
import learn.models.Task;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class TaskJdbcTemplateRepository  implements TaskRepository {

	private final JdbcTemplate jdbcTemplate;

	public TaskJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<Task> findAll() {
		final String sql = "select task_id, task_name, due_date, status, checklist_id from task " +
		                   "limit 1000;";
		return jdbcTemplate.query(sql, new TaskMapper());
	}

	//TODO Do we even need a find by ID for tasks? Is find all sufficient
	@Override
	@Transactional
	public Task findById(int taskId) {

		final String sql = "select * from task where task_id = ?;"; // km -tried this adjustment trying to fix test,
		// but no difference expected & didn't work

//		final String sql = "select task_id, task_name, due_date, status, checklist_id" +
//		                   " from task where task_id = ?;";

		Task task = jdbcTemplate.query(sql, new TaskMapper(), taskId).stream()
		                        .findFirst().orElse(null);

//		if (task != null) {
//			add(task);
//		}

		return task;
	}


	@Override
	public Task add(Task task) {

		final String sql = "insert into task (task_name, due_date, status, checklist_id) values (?,?,?,?);";

		KeyHolder keyHolder = new GeneratedKeyHolder();
		int rowsAffected = jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, task.getTaskName());
			ps.setDate(2, task.getDueDate() == null ? null : Date.valueOf(task.getDueDate()));
			ps.setBoolean(3, task.getStatus());
			ps.setInt(4, task.getChecklistId());
			return ps;
		}, keyHolder);

		if (rowsAffected <= 0) {
			return null;
		}

		task.setTaskId(keyHolder.getKey().intValue());
		return task;
	}

	//task_id, task_name, due_date, status, checklist_id
	@Override
	public boolean update(Task task) {
		final String sql = "update task set task_name = ?, due_date = ?, status = ?, checklist_id = ? where task_id = ?;";
		return jdbcTemplate.update(sql, task.getTaskName(), task.getDueDate(), task.getStatus(), task.getChecklistId(), task.getTaskId()) > 0;
	}

	
	@Override
	@Transactional
	public boolean deleteById(int taskId) {
		return jdbcTemplate.update("delete from task where task_id = ?;", taskId) > 0;
	}


}
