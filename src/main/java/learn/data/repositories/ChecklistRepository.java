package learn.data.repositories;

import learn.models.Checklist;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ChecklistRepository {

    List<Checklist> findAll();

    @Transactional
    Checklist findById(int checklistId);

    Checklist add(Checklist checklist);

    boolean update(Checklist checklist);

    @Transactional
    boolean deleteById(int checklistId);

    List<Checklist> findAllByUserId(int userId);
}
