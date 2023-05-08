package learn.controllers;

import learn.domain.Result;
import learn.domain.UserService;
import learn.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5500", "http://127.0.0.1:5500"})
@RequestMapping("/api/user")
public class UserController {

	private final UserService service;

	public UserController(UserService service) {
		this.service = service;
	}

	@GetMapping
	public List<User> findAll() {
		return service.findAll();
	}

	@GetMapping("/{userId}")
	public User findById(@PathVariable int userId) {
		return service.findById(userId);
	}

	//create user is in authController

	@PutMapping("/{userId}")
	public ResponseEntity<Object> update(@PathVariable int userId, @RequestBody User user) {
		if (userId != user.getUserId()) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}

		Result<User> result = service.update(user);
		if (result.isSuccess()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		return ErrorResponse.build(result);
	}

	@DeleteMapping("/{userId}")
	public ResponseEntity<Void> deleteById(@PathVariable int userId) {
		Result result = service.deleteById(userId);
		if (result.isSuccess()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

}
