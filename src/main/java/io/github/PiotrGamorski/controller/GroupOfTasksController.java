package io.github.PiotrGamorski.controller;

import io.github.PiotrGamorski.logic.GroupOfTasksService;
import io.github.PiotrGamorski.model.Task;
import io.github.PiotrGamorski.model.TaskRepository;
import io.github.PiotrGamorski.model.projection.GroupOfTasksReadModel;
import io.github.PiotrGamorski.model.projection.GroupOfTasksWriteModel;
import io.github.PiotrGamorski.model.projection.TaskWriteModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

// @RestController was exchanged with @Controller in order to return both JSON and THYMELEAF TEMPLATES/STRING.
@Controller
@IllegalExceptionProcessing
@RequestMapping("/groups")
class GroupOfTasksController {
    private final String view = "groupsView";

    private static final Logger logger = LoggerFactory.getLogger(GroupOfTasksController.class);
    private final TaskRepository taskRepository;
    private final GroupOfTasksService groupOfTasksService;

    @Autowired
    GroupOfTasksController(final TaskRepository taskRepository, final GroupOfTasksService groupOfTasksService){
        this.taskRepository = taskRepository;
        this.groupOfTasksService = groupOfTasksService;
    }

    // VIEW CONTROLLER
    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
    String showGroups(Model model){
        model.addAttribute("GroupOfTasksWriteModel", new GroupOfTasksWriteModel());
        return view;
    }

    @PostMapping(produces = MediaType.TEXT_HTML_VALUE, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    String addGroup(@ModelAttribute("GroupOfTasksWriteModel") @Valid GroupOfTasksWriteModel current,
                    BindingResult bindingResult,
                    Model model
    ){
        if(bindingResult.hasErrors()){
            return view;
        }
        this.groupOfTasksService.createGroup(current);
        model.addAttribute("GroupOfTasksWriteModel", new GroupOfTasksWriteModel());
        model.addAttribute("Groups", this.getGroupsOfTasksReadModel());
        model.addAttribute("message", "A group has been added.");
        return view;
    }

    @PostMapping(params = "addTask", produces = MediaType.TEXT_HTML_VALUE)
    String addGroupTask(@ModelAttribute("GroupOfTasksWriteModel") GroupOfTasksWriteModel current){
        current.getTasks().add(new TaskWriteModel());
        return view;
    }

    @ModelAttribute("Groups")
    List<GroupOfTasksReadModel> getGroupsOfTasksReadModel(){
        return this.groupOfTasksService.readAll();
    }

    // REST CONTROLLER
    @Transactional
    @ResponseBody
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<GroupOfTasksReadModel> createGroup(@RequestBody @Valid GroupOfTasksWriteModel toCreate){
        var result = groupOfTasksService.createGroup(toCreate);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @ResponseBody
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<GroupOfTasksReadModel>> readAllGroups(){
        logger.warn("Exposing all the tasks");
        return ResponseEntity.ok(groupOfTasksService.readAll());
    }

    @ResponseBody
    @GetMapping("/{id}")
    ResponseEntity<List<Task>> readAllTasksFromGroup(@PathVariable int id){
        logger.warn("Exposing all the tasks");
        List<Task> result = taskRepository.findAllByGroup_Id(id);
        if (result.isEmpty()){
            return ResponseEntity.notFound().build();
        } else {
            return new ResponseEntity<>(result, HttpStatus.valueOf(200));
        }
    }

    @Transactional
    @ResponseBody
    @PatchMapping("/{id}")
    public ResponseEntity<?> toggleGroup(@PathVariable int id){
        groupOfTasksService.toggleDonePropInGroup(id);
        return ResponseEntity.noContent().build();
    }
}
