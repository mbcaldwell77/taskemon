package learn.domain;

import learn.data.repositories.PokemonRepository;

import learn.models.Pokemon;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;


import static learn.TestHelper.makeBadPokemon;
import static learn.TestHelper.makePokemon;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class PokemonServiceTest {

    @Autowired
    PokemonService service;

    @MockBean
    PokemonRepository repository;

    @Test
    void shouldFindBulbasaur() { //findById
        // pass-through test, probably not useful
        Pokemon expected = makePokemon();
        when(repository.findById(1)).thenReturn(expected);
        Pokemon actual = service.findById(1);
        assertEquals(expected, actual);
    }

    @Test
    void shouldNotAddWhenInvalid() { //add test
        Pokemon pokemon = makeBadPokemon();
        Result<Pokemon> result = service.add(pokemon);
        assertEquals(ResultType.INVALID, result.getType());

        pokemon.setPokemonId(0);
        pokemon.setPokemonName(null);
        result = service.add(pokemon);
        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldAddWhenValid() { //add test
        Pokemon expected = makePokemon();
        Pokemon arg = makePokemon();
        arg.setPokemonId(0);

        when(repository.add(arg)).thenReturn(expected);
        Result<Pokemon> result = service.add(arg);
        assertEquals(ResultType.SUCCESS, result.getType());

        assertEquals(expected, result.getPayload());
    }
}
