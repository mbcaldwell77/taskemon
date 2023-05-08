package learn.controllers;


import learn.domain.ChecklistService;
import learn.domain.PokemonService;
import learn.domain.Result;
import learn.models.Checklist;
import learn.models.Pokemon;
import learn.models.Task;
import learn.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5500", "http://127.0.0.1:5500"})
@RequestMapping("/api/pokemon")
public class PokemonController {

    private final PokemonService service;
    public PokemonController(PokemonService service) { this.service = service; }

    @GetMapping
    public List<Pokemon> findAll() { return service.findAll(); }

    @GetMapping("/{pokemonId}")
    public Pokemon findById(@PathVariable int pokemonId) { return service.findById(pokemonId); }

    @PostMapping
    public ResponseEntity<Object> add(@RequestBody Pokemon pokemon) {
        Result<Pokemon> result = service.add(pokemon);
        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }

        return ErrorResponse.build(result);
    }


    @PutMapping("/{pokemonId}")
    public ResponseEntity<Object> update(@PathVariable int pokemonId, @RequestBody Pokemon pokemon) {
        if (pokemonId != pokemon.getPokemonId()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Result<Pokemon> result = service.update(pokemon);
        if (result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return ErrorResponse.build(result);
    }

    @DeleteMapping("/{pokemonId}")
    public ResponseEntity<Void> deleteById(@PathVariable int pokemonId, @AuthenticationPrincipal User user) {
    List<String> authorities = user.getAuthorities().stream()
            .map(a -> a.getAuthority())
            .toList();

    Pokemon pokemon = service.findById(pokemonId);
		if (authorities.contains("ADMIN")) {
        service.deleteById(pokemonId);
    } else {
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
		return null;
}

}
