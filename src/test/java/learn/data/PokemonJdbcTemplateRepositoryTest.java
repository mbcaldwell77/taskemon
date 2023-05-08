package learn.data;

import learn.data.jdbctemplaterepositories.PokemonJdbcTemplateRepository;
import learn.models.Pokemon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static learn.TestHelper.makePokemon;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class PokemonJdbcTemplateRepositoryTest {

    final static int NEXT_ID = 4; //might need to adjust, depending on final known good state

    @Autowired
    PokemonJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }

    @Test
    void shouldFindAll() {
        List<Pokemon> pokemons = repository.findAll();
        assertNotNull(pokemons);

        // can't predict order
        // if delete is first, we're down to 1
        // if add is first, we may go as high as 3
        assertTrue(pokemons.size() >= 1 && pokemons.size() <= 4);
    }

    @Test
    void shouldFindCharmander() {
        Pokemon charmander = repository.findById(4); // current value used in taskemon_test
        assertEquals(4, charmander.getPokemonId());
        assertEquals("Charmander", charmander.getPokemonName());
        assertEquals("https://img.pokemondb.net/artwork/large/charmander.jpg", charmander.getImgUrl());
    }

    @Test
    void shouldAdd() {
        // all fields
        Pokemon pokemon = makePokemon(); // to create TestHelper and makePokemon method in it
        Pokemon actual = repository.add(pokemon);
        assertNotNull(actual);
        assertEquals(NEXT_ID + 1, actual.getPokemonId());
    }

    @Test
    void shouldUpdate() {
        Pokemon pokemon = makePokemon();
        pokemon.setPokemonId(2);
        assertTrue(repository.update(pokemon));
    }

    @Test
    void shouldNotUpdate() { //should not update pokemon that doesn't exist
        Pokemon pokemon = makePokemon();
        pokemon.setPokemonId(100);
        assertFalse(repository.update(pokemon));
    }

    @Test
    void shouldDelete() {
        assertTrue(repository.deleteById(1));
    }

}
