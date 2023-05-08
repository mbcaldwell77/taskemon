package learn.data.mappers;

import learn.models.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserMapper implements RowMapper<User> {

	private final List<String> roles;
	public UserMapper(List<String> roles){ this.roles = roles;}

	@Override
	public User mapRow(ResultSet rs, int i) throws SQLException{
		return new User(
				rs.getInt("user_id"),
				rs.getString("username"),
				rs.getString("password_hash"),
				rs.getString("email"),
				rs.getBoolean("enabled"),
				roles);
	}


}
