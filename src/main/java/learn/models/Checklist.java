package learn.models;


import java.util.List;

public class Checklist {


    public Checklist(int checklistId, int totalCompletion, String checklistName, int pokemonId, Pokemon pokemon, int userId) {
        this.checklistId = checklistId;
        this.totalCompletion = totalCompletion;
        this.checklistName = checklistName;
        this.pokemonId = pokemonId;
        this.pokemon = pokemon;
        this.userId = userId;
    }

    public Checklist(){}

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    private List<Task> tasks;

    private int checklistId;
    private int totalCompletion;

    private String checklistName;

    private int pokemonId;

    private Pokemon pokemon;

    private int userId;

    public Pokemon getPokemon() {
        return pokemon;
    }

    public void setPokemon(Pokemon pokemon) { this.pokemon = pokemon;
    }

    public int getChecklistId() {
        return checklistId;
    }

    public void setChecklistId(int checklistId) {
        this.checklistId = checklistId;
    }

    public int getTotalCompletion() {
        //calculate
        if (tasks == null || tasks.size() == 0){
            return totalCompletion;
        }
        double numOfTasks = this.tasks.size();
        double numOfCompletedTasks = this.tasks.stream().filter(Task::getStatus).count();
        double completePercent = ((numOfCompletedTasks / numOfTasks) * 100);
        return (int) completePercent;
    }

    public void setTotalCompletion(int totalCompletion) {
        this.totalCompletion = totalCompletion;
    }

    public String getChecklistName() {
        return checklistName;
    }

    public void setChecklistName(String checklistName) {
        this.checklistName = checklistName;
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
