package learn.data.repositories;

import learn.models.Pokemon;
import learn.models.PokemonUser;
import learn.models.Task;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PokemonUserRepository {

    boolean add(PokemonUser pokemonUser);

    boolean update(PokemonUser pokemonUser);

    @Transactional
    boolean deleteByKey(int pokemonId, int userId);
}
