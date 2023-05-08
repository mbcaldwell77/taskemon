package learn.data.jdbctemplaterepositories;


import learn.controllers.ChecklistController;
import learn.data.mappers.ChecklistMapper;
import learn.data.mappers.PokemonMapper;
import learn.data.mappers.PokemonUserMapper;
import learn.data.mappers.TaskMapper;
import learn.data.repositories.ChecklistRepository;
import learn.models.*;
import org.springframework.boot.autoconfigure.web.ConditionalOnEnabledResourceChain;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class ChecklistJdbcTemplateRepository implements ChecklistRepository {
    private final JdbcTemplate jdbcTemplate;

    public ChecklistJdbcTemplateRepository(JdbcTemplate jdbcTemplate){ this.jdbcTemplate = jdbcTemplate; }

    @Override
    public List<Checklist> findAll(){
        final String sql = "select * from checklist";
        return jdbcTemplate.query(sql, new ChecklistMapper());
    }

    @Override
    public List<Checklist> findAllByUserId(int userId) {
        final String sql = "select * from checklist where user_id = ?;";
        return jdbcTemplate.query(sql, new ChecklistMapper(), userId);
    }


    @Override
    @Transactional
    public Checklist findById(int checklistId) {
        final String sql = "select * from checklist where checklist_id = ?;";
        Checklist checklist = jdbcTemplate.query(sql, new ChecklistMapper(), checklistId).stream()
                .findFirst().orElse(null);

        if (checklist != null){
            addPokemon(checklist);
            addTask(checklist);
        }

        return checklist;
    }

    @Override
    public Checklist add(Checklist checklist){
        final String sql = "insert into checklist (checklist_name, total_completion, pokemon_id, user_id) values (?,?,?,?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, checklist.getChecklistName());
            ps.setInt(2, checklist.getTotalCompletion());
            ps.setInt(3, checklist.getPokemonId());
            ps.setInt(4, checklist.getUserId());
            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }

        checklist.setChecklistId(keyHolder.getKey().intValue());
        return checklist;
    }

    @Override
    public boolean update(Checklist checklist) {
        final String sql = "update checklist set total_completion = ?, checklist_name = ?, pokemon_id = ?, user_id = ? where checklist_id = ?;";
        return jdbcTemplate.update(sql, checklist.getTotalCompletion(), checklist.getChecklistName(), checklist.getPokemonId(), checklist.getUserId(), checklist.getChecklistId()) > 0;
    }

    @Override
    @Transactional
    public boolean deleteById(int checklistId){
        jdbcTemplate.update("delete from task where checklist_id = ?;", checklistId);
        return jdbcTemplate.update("delete from checklist where checklist_id = ?;", checklistId) > 0;
    }


    private void addPokemon(Checklist checklist){
        String sql = "select pokemon_id, pokemon_name, img_url from pokemon where pokemon_id = ?;";
        Pokemon pokemon = jdbcTemplate.queryForObject(sql, new PokemonMapper(), checklist.getPokemonId());
        checklist.setPokemon(pokemon);
    }

    private void addTask(Checklist checklist){
        String sql = "Select task_id, task_name, due_date, status, checklist_id from task where checklist_id = ?;";
        List<Task> tasks = jdbcTemplate.query(sql, new TaskMapper(), checklist.getChecklistId());
        checklist.setTasks(tasks);
    }

}
