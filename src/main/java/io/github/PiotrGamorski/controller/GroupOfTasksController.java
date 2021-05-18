package io.github.PiotrGamorski.controller;

import io.github.PiotrGamorski.logic.GroupOfTasksService;
import io.github.PiotrGamorski.model.Task;
import io.github.PiotrGamorski.model.TaskRepository;
import io.github.PiotrGamorski.model.projection.GroupOfTasksReadModel;
import io.github.PiotrGamorski.model.projection.GroupOfTasksWriteModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/groups")
class GroupOfTasksController {
    private static final Logger logger = LoggerFactory.getLogger(GroupOfTasksController.class);
    private final TaskRepository taskRepository;
    private final GroupOfTasksService groupOfTasksService;

    @Autowired
    GroupOfTasksController(final TaskRepository taskRepository, final GroupOfTasksService groupOfTasksService){
        this.taskRepository = taskRepository;
        this.groupOfTasksService = groupOfTasksService;
    }

    @Transactional
    @PostMapping
    ResponseEntity<GroupOfTasksReadModel> createGroup(@RequestBody @Valid GroupOfTasksWriteModel toCreate){
        var result = groupOfTasksService.createGroup(toCreate);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @GetMapping
    ResponseEntity<List<GroupOfTasksReadModel>> readAllGroups(){
        logger.warn("Exposing all the tasks");
        return ResponseEntity.ok(groupOfTasksService.readAll());
    }

    @GetMapping("/{id}")
    ResponseEntity<List<Task>> readAllTasksFromGroup(@PathVariable int id){
        logger.warn("Exposing all the tasks");
        return ResponseEntity.ok(taskRepository.findAllByGroup_Id(id));
    }

    @Transactional
    @PatchMapping("/{id}")
    public ResponseEntity<?> toggleGroup(@PathVariable int id){
        groupOfTasksService.toggleDonePropInGroup(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e){
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(IllegalStateException.class)
    ResponseEntity<String> handleIllegalStateException(IllegalStateException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
