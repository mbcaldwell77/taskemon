package learn;

import learn.models.Checklist;
import learn.models.Pokemon;
import learn.models.Task;
import learn.models.User;

import java.time.LocalDate;

public class TestHelper {

    public static Pokemon makePokemon() {
        Pokemon pokemon = new Pokemon();
        pokemon.setPokemonName("Testasaur");
        pokemon.setImgUrl("https://assets.pokemon.com/assets/cms2/img/pokedex/full/001.png");
        return pokemon;
    }

    public static Pokemon makeBadPokemon() {
        Pokemon pokemon = new Pokemon();
        pokemon.setPokemonId(1);
        pokemon.setPokemonName("Testasaur");
        pokemon.setImgUrl("https://assets.pokemon.com/assets/cms2/img/pokedex/full/001.png");
        return pokemon;
    }


//    insert into task(task_id, task_name, due_date, status, checklist_id) values
//-- chores 50%
//(1, 'dishes', '2024-01-01', 'not complete', 1),
    public static Task makeTask() {
        Task task = new Task();
        task.setTaskId(1);
        task.setTaskName("dishes");
        task.setDueDate(LocalDate.of(2024,01,01));
        task.setStatus(false);
        task.setChecklistId(1);
        return task;
    }



    public static Checklist makeChecklist() {
        Checklist checklist = new Checklist();
        checklist.setChecklistId(6);
        checklist.setTotalCompletion(65);
        checklist.setChecklistName("Testlist");
        checklist.setPokemonId(4);
        checklist.setUserId(1);
        return checklist;
    }

    public static User makeUser() {
        // username, password_hash, email, enabled
        User user = new User();
        user.setUserId(4);
        user.setUsername("Balthazar");
        user.setPassword("Baltspassword1");
        user.setEmail("balty.zary@test.com");
        user.setEnabled(true);
        return user;
    }

    public static User makeBadUser() {
        User user = new User();
        user.setUserId(1); // badId -already being used
        user.setUsername("Balthazar");
        user.setPassword("Baltspassword1");
        user.setEmail("balty.zary@test.com");
        user.setEnabled(true);
        return user;
    }

}
