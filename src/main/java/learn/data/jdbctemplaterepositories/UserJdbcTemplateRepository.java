package learn.data.jdbctemplaterepositories;

import learn.data.mappers.UserMapper;
import learn.models.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;

@Repository
public class UserJdbcTemplateRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserJdbcTemplateRepository (JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<User> mapper = (resultSet, index) -> {
        User user = new User();
        user.setUserId(resultSet.getInt("user_id"));
        user.setUsername(resultSet.getString("username"));
        user.setPassword(resultSet.getString("password_hash"));
        user.setEmail(resultSet.getString("email"));
        return user;
    };

    public List<User> findAll() {
        final String sql = "select * from user";
        return jdbcTemplate.query(sql, mapper);
    }


    @Transactional
    public User findById(int userId) {
        final String sql = "select * from user where user_id = ?;";
        User user = jdbcTemplate.query(sql, mapper, userId).stream()
                .findFirst().orElse(null);
        if (user != null) {
            addAuthorities(user);
        }

        return user;
    }

//    @Transactional
//    public User findByUsername(String username) {
//        List<String> roles = getRolesByUsername(username);
//
//        final String sql = "select user_id, username, password_hash, email, enabled from user where username = ?";
//        return jdbcTemplate.query(sql, new UserMapper(roles), username).stream().findFirst().orElse(null);
//    }

    public User findByUsername(String username) {

        String sql = "select user_id, username, password_hash, email from user where username = ?;";
        User user = jdbcTemplate.query(sql, mapper, username).stream()
                .findFirst().orElse(null);

        if (user != null) {
            addAuthorities(user);
        }
        return user;
    }

    @Transactional
    public User create (User user){
        final String sql = "insert into user (username, password_hash, email) values (?,?,?);";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getEmail());
            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }

        user.setUserId(keyHolder.getKey().intValue());
        return user;
    }

    @Transactional
    public boolean update(User user) {

        final String sql = "update user set "
                + "username = ?, "
                + "email = ?, "
                + "enabled = ? "
                + "where user_id = ?";

        jdbcTemplate.update(sql,
                user.getUsername(), user.getEmail(), user.isEnabled(), user.getUserId());

        return jdbcTemplate.update(sql,
                user.getUsername(), user.getEmail(), user.isEnabled(), user.getUserId()) > 0;
    }


//    private void updateRoles(User user) {
//        // delete all roles, then re-add
//        jdbcTemplate.update("delete from user_auth where user_id = ?;", user.getUserId());
//
//        Collection<GrantedAuthority> authorities = user.getAuthorities();
//
//        if (authorities == null) {
//            return;
//        }
//
//        for (GrantedAuthority auth : authorities) {
//            String sql = "insert into user_auth (user_id, auth_id) "
//                    + "select ?, auth_id from auth where `name` = ?;";
//            jdbcTemplate.update(sql, user.getUserId(), auth.getAuthority());
//        }
//    }
//
//
//    private List<String> getRolesByUsername(String username) {
//        final String sql = "select u.name "
//                + "from user_auth ua "
//                + "inner join auth a on ua.role_id = a.role_id "
//                + "inner join user u on ua.user_id = u.user_id "
//                + "where u.username = ?";
//        return jdbcTemplate.query(sql, (rs, rowId) -> rs.getString("name"), username);
//    }

    private void addAuthorities(User user) {
        String sql = "select a.name "
                + "from user_auth as ua "
                + "inner join auth as a on ua.auth_id = a.auth_id "
                + "where ua.user_id = ?";


        List<String> authorities = jdbcTemplate.query(
                sql,
                (rs, i) -> rs.getString("name"),
                user.getUserId()
        );
        user.addAuthorities(authorities);
    }


    @Transactional // SQL seems to be correct/working...
    public boolean deleteById(int userId) { //CHECK ON OTHER DELETIONS NEEDED
        jdbcTemplate.update("delete from checklist where user_id = ?", userId);
        jdbcTemplate.update("delete from pokemon_user where user_id = ?", userId);
        jdbcTemplate.update("delete from user_auth where user_id = ?", userId);
        return jdbcTemplate.update("delete from user where user_id = ?;", userId) > 0;
    }
}



