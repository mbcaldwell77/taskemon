package learn.data.repositories;

import learn.models.Pokemon;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PokemonRepository {
    List<Pokemon> findAll();

    @Transactional
    Pokemon findById(int pokemonId);

    Pokemon add(Pokemon pokemon);

    boolean update(Pokemon pokemon);

    @Transactional
    boolean deleteById(int pokemonId);
}
