package io.github.PiotrGamorski.controller;

import io.github.PiotrGamorski.logic.ProjectService;
import io.github.PiotrGamorski.model.Project;
import io.github.PiotrGamorski.model.ProjectStep;
import io.github.PiotrGamorski.model.projection.ProjectWriteModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/projects")
class ProjectViewController {
     final String view = "projectsView";
     final ProjectService projectService;

     @Autowired
     public ProjectViewController(ProjectService projectService){
         this.projectService = projectService;
     }

    @GetMapping
    String showProjects(Model model){
        model.addAttribute("ProjectWriteModel", new ProjectWriteModel());
        return this.view;
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

    @ModelAttribute("Projects")
    List<Project> getProjects(Project project){
         return projectService.readAll();
     }
}
