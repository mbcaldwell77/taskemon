package learn.data.mappers;

import learn.models.Checklist;
import learn.models.Pokemon;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ChecklistMapper implements RowMapper<Checklist> {

	@Override
	public Checklist mapRow(ResultSet resultSet, int i) throws SQLException {
		Checklist checklist = new Checklist();
		checklist.setChecklistId(resultSet.getInt("checklist_id"));
		checklist.setTotalCompletion(resultSet.getInt("total_completion"));
		checklist.setChecklistName(resultSet.getString("checklist_name"));
		checklist.setPokemonId(resultSet.getInt("pokemon_id"));
		checklist.setUserId(resultSet.getInt("user_id"));

		return checklist;
	}
}
