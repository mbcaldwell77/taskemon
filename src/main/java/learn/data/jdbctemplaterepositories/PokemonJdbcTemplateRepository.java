package learn.data.jdbctemplaterepositories;

import learn.data.mappers.PokemonMapper;
import learn.data.repositories.PokemonRepository;
import learn.models.Pokemon;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class PokemonJdbcTemplateRepository implements PokemonRepository {

    private final JdbcTemplate jdbcTemplate;

    public PokemonJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Pokemon> findAll() {
        final String sql = "select * from pokemon;";
        return jdbcTemplate.query(sql, new PokemonMapper());
    }


    @Override
    @Transactional
    public Pokemon findById(int pokemonId) {
        final String sql = "select * from pokemon where pokemon_id = ?;";

        Pokemon pokemon = jdbcTemplate.query(sql, new PokemonMapper(), pokemonId).stream()
                .findAny().orElse(null);

//        if (result != null) {
//            addUsers(result);
//            addChecklists(result);
//        }

        return pokemon;
    }

    @Override
    public Pokemon add(Pokemon pokemon) {

        final String sql = "insert into pokemon (pokemon_name, img_url) values (?,?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, pokemon.getPokemonName());
            ps.setString(2, pokemon.getImgUrl());
            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }

        pokemon.setPokemonId(keyHolder.getKey().intValue());
        return pokemon;
    }

    @Override
    public boolean update(Pokemon pokemon) {
        final String sql = "update pokemon set pokemon_name = ?, img_url = ? where pokemon_id = ?;";
        return jdbcTemplate.update(sql, pokemon.getPokemonName(), pokemon.getImgUrl(), pokemon.getPokemonId()) > 0;
    }

    @Override
    public boolean deleteById(int pokemonId) {
        jdbcTemplate.update("delete from checklist where pokemon_id = ?", pokemonId);
        jdbcTemplate.update("delete from pokemon_user where pokemon_id = ?", pokemonId);
        return jdbcTemplate.update("delete from pokemon where pokemon_id = ?", pokemonId) > 0;
    }
}


