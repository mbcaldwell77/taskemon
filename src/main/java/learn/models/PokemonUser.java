package learn.models;

public class PokemonUser {

    private int pokemonId;

    private int userId;

    public PokemonUser() {
    }
    public PokemonUser(int pokemonId, int userId) {
        this.pokemonId = pokemonId;
        this.userId = userId;
    }

    public int getPokemonId() {
        return pokemonId;
    }

    public void setPokemonId(int pokemonId) {
        this.pokemonId = pokemonId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
