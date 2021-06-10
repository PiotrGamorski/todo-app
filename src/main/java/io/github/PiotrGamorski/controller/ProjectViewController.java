package io.github.PiotrGamorski.controller;

import io.github.PiotrGamorski.logic.ProjectService;
import io.github.PiotrGamorski.model.Project;
import io.github.PiotrGamorski.model.ProjectStep;
import io.github.PiotrGamorski.model.projection.ProjectWriteModel;
import io.micrometer.core.annotation.Timed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

// This class represents pure VIEW controller
@Controller
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/projects")
class ProjectViewController {
     final String view = "projectsView";
     final ProjectService projectService;

     @Autowired
     public ProjectViewController(ProjectService projectService){
         this.projectService = projectService;
     }

    @GetMapping
    String showProjects(Model model, Authentication auth, Principal p){
//         if (auth.getAuthorities().stream()
//                 .anyMatch(a->a.getAuthority().equals("ROLE_ADMIN"))
//         ){
             model.addAttribute("ProjectWriteModel", new ProjectWriteModel());
             return this.view;
//         }
//        return "mainPage";
    }

    @PostMapping
    String addProject(@ModelAttribute("ProjectWriteModel") @Valid ProjectWriteModel currentProject,
                      BindingResult bindingResult,
                      Model model){
         if(bindingResult.hasErrors()){
             return this.view;
         }
        this.projectService.save(currentProject);
        model.addAttribute("ProjectWriteModel", new ProjectWriteModel());
        model.addAttribute("message", "New project has been added");
        return view;
    }

    @PostMapping(params = "addStep")
    String addProjectStep(@ModelAttribute("ProjectWriteModel") ProjectWriteModel currentProject){
        currentProject.getSteps().add(new ProjectStep());
        return this.view;
    }

    @Timed(value = "project.create.group", histogram = true, percentiles = {0.5, 0.95, 0.99})
    @PostMapping("/{id}")
    String createGroup(@ModelAttribute("ProjectWriteModel") ProjectWriteModel currentProject,
                       Model model,
                       @PathVariable int id,
                       @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime deadline
    ){
         try {
             projectService.createGroup(deadline, id);
             model.addAttribute("Projects", getProjects());
             model.addAttribute("message", "The group has been added.");
         } catch (IllegalStateException | IllegalArgumentException e){
             model.addAttribute("message", "An error occurred when tried to create group.");
         }
         return this.view;
    }

    // Each model in enhanced with attribute "Projects". The attribute is bind with return value of the method.
    @ModelAttribute("Projects")
    List<Project> getProjects(){
         return projectService.readAll();
     }
}
