package learn.data;

import learn.data.jdbctemplaterepositories.UserJdbcTemplateRepository;
import learn.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class UserJdbcTemplateRepositoryTest {

	@Autowired
	UserJdbcTemplateRepository repository;

	@Autowired
	KnownGoodState knownGoodState;

	@BeforeEach
	void setup() {
		knownGoodState.set();
	}

	@Test
	void shouldFindAll() {
		List<User> user = repository.findAll();
		assertNotNull(user);

		// can't predict order
		// if delete is first, we're down to 2
		// if add is first, we may go as high as 4
		assertTrue(user.size() >= 2 && user.size() <= 4);
	}

	@Test
	void shouldFindBob() {
		User user = repository.findByUsername("bob@jones.com");
		assertEquals("bob@jones.com", user.getUsername());
	}

	@Test
	void shouldAddUser() {
		User user = new User();
		user.setUsername("TEST");
		user.setPassword("Password1!");
		user.setEmail("testemail@email.com");
		User actual = repository.create(user);
		assertNotNull(actual);
		assertEquals("TEST", actual.getUsername());
	}

	@Test
	void shouldUpdateBob() {
		User user = new User();
		user.setUserId(3);
		user.setUsername("TestyBobby");
		user.setPassword("Testword3!");
		user.setEmail("testmail@test.com");
		user.setEnabled(true);
		assertTrue(repository.update(user));
		assertEquals("TestyBobby", user.getUsername());
		assertEquals("Testword3!", user.getPassword());
		assertEquals("testmail@test.com", user.getEmail());
	}

	@Test
	void shouldDeleteBob() { // should delete user being used in other tables
		assertTrue(repository.deleteById(3));
	}
}


