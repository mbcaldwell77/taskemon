package learn.data.mappers;

import learn.models.Pokemon;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PokemonMapper implements RowMapper<Pokemon> {
	@Override
	public Pokemon mapRow(ResultSet resultSet, int i) throws SQLException {
		Pokemon pokemon = new Pokemon();
		pokemon.setPokemonId(resultSet.getInt("pokemon_id"));
		pokemon.setPokemonName(resultSet.getString("pokemon_name"));
		pokemon.setImgUrl(resultSet.getString("img_url"));
		return pokemon;
	}
}
