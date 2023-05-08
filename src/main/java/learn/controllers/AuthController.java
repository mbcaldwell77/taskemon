package learn.controllers;

import learn.domain.ChecklistService;
import learn.domain.Result;
import learn.models.User;
import learn.security.JwtConverter;
import learn.domain.UserService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@ConditionalOnWebApplication
public class AuthController {

    private final AuthenticationManager manager;
    private final JwtConverter converter;
    private final UserService userService;
    private final ChecklistService checklistService;

    public AuthController(AuthenticationManager manager,
                          JwtConverter converter,
                          UserService userService, ChecklistService checklistService) {
        this.manager = manager;
        this.converter = converter;
        this.userService = userService;
        this.checklistService = checklistService; }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody User user) {

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                user.getUsername(), user.getPassword());

        try {
            Authentication authentication = manager.authenticate(token);
            if (authentication.isAuthenticated()) {
                User authenticatedUser = (User) authentication.getPrincipal();
                String jwt = converter.userToToken(authenticatedUser);
                HashMap<String, String> map = new HashMap<>();
                map.put("jwt", jwt);
                return new ResponseEntity<>(map, HttpStatus.OK);
            }
        } catch (AuthenticationException ex) {
            System.out.println(ex.getMessage());
        }

        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@AuthenticationPrincipal User user){
        String jwt = converter.userToToken(user);
        HashMap<String, String> map = new HashMap<>();
        map.put("jwt", jwt);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PostMapping("/create-account")
    public ResponseEntity<?> createAccount(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");
        String email = credentials.get("email");

        Result<User> result = userService.create(username, password, email);


        if (!result.isSuccess()) {
            return new ResponseEntity<>(result.getMessages(), HttpStatus.BAD_REQUEST);
        }


        HashMap<String, Integer> map = new HashMap<>();
        map.put("userId", result.getPayload().getUserId());

        return new ResponseEntity<>(map, HttpStatus.CREATED);
    }
}
