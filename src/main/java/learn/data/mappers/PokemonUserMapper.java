package learn.data.mappers;
import learn.models.PokemonUser;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PokemonUserMapper implements RowMapper<PokemonUser> {

	@Override
	public PokemonUser mapRow(ResultSet resultSet, int i) throws SQLException {

		PokemonUser pokemonUser = new PokemonUser();
		pokemonUser.setPokemonId(resultSet.getInt("pokemon_id"));
		pokemonUser.setUserId(resultSet.getInt("user_id"));

		return pokemonUser;
	}
}
