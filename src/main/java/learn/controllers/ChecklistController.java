package learn.controllers;

import learn.domain.ChecklistService;
import learn.domain.Result;
import learn.models.Checklist;
import learn.models.Task;
import learn.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5500", "http://127.0.0.1:5500", "http://localhost:8081"})
@RequestMapping("/api/checklist")
public class ChecklistController {

    private final ChecklistService service;

    public ChecklistController(ChecklistService service) { this.service = service; }

    @GetMapping
    public List<Checklist> findAll(@AuthenticationPrincipal User user) {
        List<String> authorities = user.getAuthorities().stream()
                .map(a -> a.getAuthority())
                .toList();
        if (authorities.contains("ADMIN")) {
            return service.findAll();
        } else if (authorities.contains("USER")){
            return service.findAllByUser(user);
        }
         return new ArrayList<>();
         }

//    @GetMapping
//    public List<Checklist> findAll(){
//        List<Checklist> allChecklists = service.findAll();
//        return allChecklists;
//    }

    @GetMapping("/{checklistId}")
    public Checklist findById(@PathVariable int checklistId) {
        Checklist checklistById = service.findById(checklistId);
        return checklistById;
    }



    @PostMapping
    public ResponseEntity<Object> add(@RequestBody Checklist checklist) {
        Result<Checklist> result = service.add(checklist);
        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }

        return ErrorResponse.build(result);
    }
//
//    @PostMapping()
//    public ResponseEntity<?> add(@RequestBody Checklist checklist) {
//        Result<Checklist> result = service.add(checklist);
//
//
//        if (!result.isSuccess()) {
//            return new ResponseEntity<>(result.getMessages(), HttpStatus.BAD_REQUEST);
//        }
//
//
//        HashMap<String, Integer> map = new HashMap<>();
//        map.put("checklistId", result.getPayload().getChecklistId());
//
//        return new ResponseEntity<>(map, HttpStatus.CREATED);
//    }


    @PutMapping("/{checklistId}")
    public ResponseEntity<Object> update(@PathVariable int checklistId, @RequestBody Checklist checklist) {
        if (checklistId != checklist.getChecklistId()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Result<Checklist> result = service.update(checklist);
        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.NO_CONTENT);
        }

        return ErrorResponse.build(result);
    }

    @DeleteMapping("/{checklistId}")
    public ResponseEntity<Object> deleteChecklistById(@PathVariable int checklistId, @AuthenticationPrincipal User user) {
        List<String> authorities = user.getAuthorities().stream()
                .map(a -> a.getAuthority())
                .toList();

        Checklist list = service.findById(checklistId);
            if (list.getUserId() == user.getUserId()) {
            service.deleteById(checklistId);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
            //CHANGED FROM RETURN NULL, does this work better?
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
