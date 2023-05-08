package learn.domain;


import learn.data.jdbctemplaterepositories.UserJdbcTemplateRepository;
import learn.domain.Result;
import learn.domain.ResultType;
import learn.domain.Validations;
import learn.models.User;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserService implements UserDetailsService {

    private final UserJdbcTemplateRepository repository;
    private final PasswordEncoder encoder;

    public UserService(UserJdbcTemplateRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    public List<User> findAll() { return repository.findAll(); }
    public User findById(int userId) { return repository.findById(userId); };



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByUsername(username);
        if (user == null || !user.isEnabled()) {
            throw new UsernameNotFoundException(username + " not found");
        }
        return user;
    }

    public Result<User> create(String username, String password, String email) {
        Result<User> result = validate(username, password, email);
        if (!result.isSuccess()) {
            return result;
        }

        password = encoder.encode(password);

        User user = new User(0, username, password, email, true, List.of("USER"));

        try {
            user = repository.create(user);
            result.setPayload(user);
        } catch (DuplicateKeyException e) {
            result.addMessage("The provided username already exists", ResultType.INVALID);
        }

        return result;
    }

    public Result<User> update(User user){
        Result<User> result = validateUser(user);
        if (!result.isSuccess()){
            return result;
        }

        if (user.getUserId() <= 0){
            result.addMessage("UserId must be set for update operation", ResultType.INVALID);
            return result;
        }

        if(!repository.update(user)) {
            String message = String.format("userid: %s, not found", user.getUserId());
            result.addMessage(message, ResultType.NOT_FOUND);
            return result;
        }

        return result;
    }

    public Result<User> deleteById(int userId) {
        Result<User> result = new Result<>();
        if (userId == 1 || userId ==2) {
            result.addMessage("Cannot delete general user (id = 2) or admin (id =1) categories", ResultType.INVALID);
        } else {
            repository.deleteById(userId);
        }
        return result;
    }

    private Result<User> validateUser(User user){
        Result<User> result = new Result<>();
        if (user == null){
            result.addMessage("User cannot be null", ResultType.INVALID);
            return result;
        }
         return result;
    }

    private Result<User> validate(String username, String password, String email) {
        Result<User> result = new Result<>();
        if (username == null || username.isBlank()) {
            result.addMessage("Username is required", ResultType.INVALID);
            return result;
        }

        if (password == null) {
            result.addMessage("password is required", ResultType.INVALID);
            return result;
        }

        if (email == null){
            result.addMessage("Email is required", ResultType.INVALID);
        }

        if (!email.contains("@")){
            result.addMessage("Invalid email address", ResultType.INVALID);
        }

        if (email.length() < 1 || email.length() > 255){
            result.addMessage("Email needs to have more than one character or less than 255 characters.", ResultType.INVALID);
        }

        if (username.length() > 50) {
            result.addMessage("username must be less than 50 characters", ResultType.INVALID);
        }

        if (!isValidPassword(password)) {
            result.addMessage("password must be at least 8 character and contain a digit," +
                                        " a letter, and a non-digit/non-letter",
                    ResultType.INVALID);
        }

        return result;
    }

    private boolean isValidPassword(String password) {
        if (password.length() < 8) {
            return false;
        }

        int digits = 0;
        int letters = 0;
        int others = 0;
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) {
                digits++;
            } else if (Character.isLetter(c)) {
                letters++;
            } else {
                others++;
            }
        }

        return digits > 0 && letters > 0 && others > 0;
    }

}
