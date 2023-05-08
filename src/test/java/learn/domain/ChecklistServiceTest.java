package learn.domain;

import learn.data.DataAccessException;
import learn.data.repositories.ChecklistRepository;
import learn.domain.ChecklistService;
import learn.models.Checklist;
import learn.models.Pokemon;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.xml.crypto.Data;
import java.util.List;

import static learn.TestHelper.makeChecklist;
import static learn.TestHelper.makePokemon;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ChecklistServiceTest {

    @Autowired
    ChecklistService service;

    @MockBean
    ChecklistRepository repository;

    //(int checklistId, int totalCompletion, String checklistName, int pokemonId, Pokemon pokemon, int userId)
    @Test
    void shouldFindTwoItemsInOneChecklist() throws DataAccessException{
        when(repository.findAll()).thenReturn(List.of(
                new Checklist(1,  50, "chores", 2, makePokemon(), 1),
                new Checklist(2,  75, "errands", 3, makePokemon(), 1)
        ));
    }

    @Test
    void shouldFindChecklistWithAnIdOfTwo() throws DataAccessException {
        when(repository.findById(2)).thenReturn(new Checklist());
        Checklist checklist = service.findById(2);
        assertNotNull(checklist);
    }

    @Test
    void shouldNotAddWhenInvalid() throws DataAccessException {
        Checklist checklist = makeChecklist();
        Result<Checklist> result = service.add(checklist);
        assertEquals(ResultType.INVALID, result.getType());
        checklist.setChecklistId(0);
        checklist.setChecklistName(null);
        result = service.add(checklist);
        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldAddWhenValid() {
        Checklist expected = makeChecklist();
        Checklist arg = makeChecklist();
        arg.setChecklistId(0);

        when(repository.add(arg)).thenReturn(expected);
        Result<Checklist> result = service.add(arg);
        assertEquals(ResultType.SUCCESS, result.getType());

        assertEquals(expected, result.getPayload());


    }






}
