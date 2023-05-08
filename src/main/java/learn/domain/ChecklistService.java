package learn.domain;

import learn.data.repositories.ChecklistRepository;
import learn.models.Checklist;
import learn.models.User;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class ChecklistService {
    private final ChecklistRepository repository;
    public ChecklistService(ChecklistRepository repository) {this.repository = repository;}

    public List<Checklist> findAll(){ return repository.findAll(); }

    public List<Checklist> findAllByUser(User user) {
        return repository.findAllByUserId(user.getUserId());
    }

    public Checklist findById(int checklistId) { return repository.findById(checklistId); }

    public Result<Checklist> add(Checklist checklist){
        Result<Checklist> result = validate(checklist);
        if (!result.isSuccess()) {
            return result;
        }
        if (checklist.getChecklistId() != 0) {
            result.addMessage("ChecklistId cannot be sat for `add` operation", ResultType.INVALID);
            return result;
        }

        checklist = repository.add(checklist);
        result.setPayload(checklist);
        return result;
    }

    public Result<Checklist> update(Checklist checklist){
        Result<Checklist> result = validate(checklist);
        if (!result.isSuccess()) {
            return result;
        }

        if (checklist.getChecklistId() <= 0) {
            result.addMessage("checklistId must be set for `update` operation", ResultType.INVALID);
            return result;
        }

        if (!repository.update(checklist)) {
            String msg = String.format("checklistId: %s, not found", checklist.getChecklistId());
            result.addMessage(msg, ResultType.NOT_FOUND);
        }

        return result;
    }

    public boolean deleteById(int checklistId) { return repository.deleteById(checklistId); }

    private Result<Checklist> validate(Checklist checklist) {
        Result<Checklist> result = new Result<>();
        if(checklist == null) {
            result.addMessage("Checklist cannot be null", ResultType.INVALID);
            return result;
        }

        if (Validations.isNullOrBlank(checklist.getChecklistName())) {
            result.addMessage("Name is required", ResultType.INVALID);
            return result;
        }
        return result;
    }



}
