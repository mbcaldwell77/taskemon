package learn.data;

import learn.data.jdbctemplaterepositories.ChecklistJdbcTemplateRepository;
import learn.models.Checklist;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static learn.TestHelper.makeChecklist;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ChecklistJdbcTemplateRepositoryTest {

	final static int NEXT_ID = 6; //temp, need to set when known
	final static int USER_ID = 0; //array index starts at zero, in db this is user 1

	@Autowired
	ChecklistJdbcTemplateRepository repository;

	@Autowired
	KnownGoodState knownGoodState;

	@BeforeEach
	void setup() {
		knownGoodState.set();
	}

	@Test
	void shouldFindAll() {
		List<Checklist> checklists = repository.findAll();
		assertNotNull(checklists);
		assertTrue(checklists.size() >= 1 && checklists.size() <= 5);
	}

	@Test
	void shouldFindChores() {
		Checklist chores = repository.findById(1);
		assertEquals(1, chores.getChecklistId());
		assertEquals("chores", chores.getChecklistName());
		assertEquals(50, chores.getTotalCompletion());
		assertEquals(2, chores.getPokemonId());
		assertEquals(USER_ID, chores.getUserId());
	}

	@Test
	void shouldFindErrands() {
		Checklist errands = repository.findById(2);
		assertEquals(2, errands.getChecklistId());
		assertEquals("errands", errands.getChecklistName());
		assertEquals(75, errands.getTotalCompletion());
		assertEquals(3, errands.getPokemonId());
		assertEquals(USER_ID, errands.getUserId());
	}

	@Test
	void shouldFindGoals() {
		// NOT WORKING WHEN ALL TESTS RUN TOGETHER, BUT WORKS WHEN RUN INDIVIDUALLY
		Checklist goals = repository.findById(3);
		assertEquals(3, goals.getChecklistId());
		assertEquals("goals", goals.getChecklistName());
		assertEquals(0, goals.getTotalCompletion());
		assertEquals(4, goals.getPokemonId());
		assertEquals(USER_ID, goals.getUserId());
	}

	@Test
	void shouldFindHealth() {
		Checklist health = repository.findById(4);
		assertEquals(4, health.getChecklistId());
		assertEquals("health", health.getChecklistName());
		assertEquals(25, health.getTotalCompletion());
		assertEquals(2, health.getPokemonId());
		assertEquals(USER_ID, health.getUserId());
	}

	@Test
	void shouldFindWork() {
		Checklist work = repository.findById(5);
		assertEquals(5, work.getChecklistId());
		assertEquals("work", work.getChecklistName());
		assertEquals(100, work.getTotalCompletion());
		assertEquals(3, work.getPokemonId());
		assertEquals(USER_ID, work.getUserId());
	}

	@Test
	void shouldAdd() {
		Checklist checklist = makeChecklist();
		Checklist actual = repository.add(checklist);
		assertNotNull(actual);
		assertEquals(NEXT_ID, actual.getChecklistId());
	}

	@Test
	void shouldUpdate() {
		Checklist checklist = makeChecklist();
		checklist.setChecklistId(3);
		assertTrue(repository.update(checklist));
		checklist.setChecklistId(15);
		assertFalse(repository.update(checklist));
	}

	@Test
	void shouldDelete() {
		assertTrue(repository.deleteById(5));
		assertFalse(repository.deleteById(8));
	}
}
