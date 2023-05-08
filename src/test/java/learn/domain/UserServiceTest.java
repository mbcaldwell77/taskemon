package learn.domain;

import learn.data.jdbctemplaterepositories.UserJdbcTemplateRepository;
import learn.models.Checklist;
import learn.models.Pokemon;
import learn.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;

import static learn.TestHelper.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class UserServiceTest {


    @Autowired
    UserService service;

    @MockBean
    UserJdbcTemplateRepository repository; // CORRECT SINCE NO USERREPOSITORY?

    @Test
    void shouldFindBob() { // shouldFindBalthazar
        // "bob@jones.com", "bobspassword1!", "bob@jones.com", 1
        User actual = service.findById(3);
        assertEquals("bob@jones.com", actual.getUsername());
        assertEquals("bobspassword1!", actual.getPassword());
        assertTrue(actual.isEnabled());
    }

    @Test
    void shouldAddandFindBalthazar() { //add & findById
        // String username, String password, String email
        User expected = makeUser();
        Result<User> result = service.create("Balthazar", "Baltspassword1", "balty.zary@test.com");

        when(repository.findById(4)).thenReturn(expected);
        User actual = service.findById(4);
        assertEquals(expected, actual);
    }


    @Test
    void shouldNotAddWhenInvalid() { //add test
        Result<User> result = service.create("Balthazar", "password", "balty.zary@test.com");
        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldNotDeleteUser() { // service should not delete general user (id = 1)
        Result<User> result = service.deleteById(1);
        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldNotDeleteAdmin() { // service should not delete admin (id = 2)
        Result<User> result = service.deleteById(2);
        assertEquals(ResultType.INVALID, result.getType());
    }

}