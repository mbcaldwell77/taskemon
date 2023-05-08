package learn.data.mappers;

import learn.models.Task;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TaskMapper implements RowMapper<Task> {

	@Override
	public Task mapRow(ResultSet resultSet, int i) throws SQLException {
		Task task = new Task();
		task.setTaskId(resultSet.getInt("task_id"));
		task.setTaskName(resultSet.getString("task_name"));
		if (resultSet.getDate("due_date") != null) {
			task.setDueDate(resultSet.getDate("due_date").toLocalDate());
		}
		task.setStatus(resultSet.getBoolean("status"));
		task.setChecklistId(resultSet.getInt("checklist_id"));
		return task;
	}
}
