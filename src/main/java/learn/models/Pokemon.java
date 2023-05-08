package learn.models;

public class Pokemon {

    private int pokemonId;
    private String pokemonName;
    private String imgUrl;

    public Pokemon() {

    }

    public Pokemon(int pokemonId, String pokemonName, String imgUrl) {
        this.pokemonId = pokemonId;
        this.pokemonName = pokemonName;
        this.imgUrl = imgUrl;
    }

    public int getPokemonId() {
        return pokemonId;
    }

    public void setPokemonId(int pokemonId) {
        this.pokemonId = pokemonId;
    }

    public String getPokemonName() {
        return pokemonName;
    }

    public void setPokemonName(String pokemonName) {
        this.pokemonName = pokemonName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
