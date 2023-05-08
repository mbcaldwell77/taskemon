package learn.domain;

import learn.data.repositories.PokemonRepository;
import learn.models.Pokemon;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PokemonService {

    private final PokemonRepository repository;
    public PokemonService(PokemonRepository repository) {this.repository = repository;}

    public List<Pokemon> findAll(){ return repository.findAll(); }

    public Pokemon findById(int checklistId) { return repository.findById(checklistId); }

    public Result<Pokemon> add(Pokemon pokemon){
        Result<Pokemon> result = validate(pokemon);
        if (!result.isSuccess()) {
            return result;
        }
        if (pokemon != null && pokemon.getPokemonId() > 0) {
            result.addMessage("PokemonId cannot be set for `add` operation", ResultType.INVALID);
            return result;
        }

        pokemon = repository.add(pokemon);
        result.setPayload(pokemon);
        return result;
    }

    public Result<Pokemon> update(Pokemon pokemon){
        Result<Pokemon> result = validate(pokemon);
        if (!result.isSuccess()) {
            return result;
        }

        if (pokemon.getPokemonId() <= 0) {
            result.addMessage("PokemonId must be set for `update` operation", ResultType.INVALID);
            return result;
        }

        if (!repository.update(pokemon)) {
            String msg = String.format("pokemonId: %s, not found", pokemon.getPokemonId());
            result.addMessage(msg, ResultType.NOT_FOUND);
        }

        return result;
    }

    public boolean deleteById(int pokemonId) { return repository.deleteById(pokemonId); }

    private Result<Pokemon> validate(Pokemon pokemon) {
        Result<Pokemon> result = new Result<>();
        if(pokemon == null) {
            result.addMessage("Pokemon cannot be null", ResultType.INVALID);
            return result;
        }

        if (Validations.isNullOrBlank(pokemon.getPokemonName())) {
            result.addMessage("Name is required", ResultType.INVALID);
            return result;
        }
        return result;
    }

}
