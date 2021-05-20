package io.github.PiotrGamorski.controller;

import io.github.PiotrGamorski.logic.ProjectService;
import io.github.PiotrGamorski.model.ProjectStep;
import io.github.PiotrGamorski.model.projection.ProjectWriteModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/projects")
class ProjectViewController {
     final String attributeName = "ProjectWriteModel";
     final String view = "projects";
     final ProjectService projectService;

     @Autowired
     public ProjectViewController(ProjectService projectService){
         this.projectService = projectService;
     }

    @GetMapping
    String showProjects(Model model){
        var attributeValue = new ProjectWriteModel();
        attributeValue.setDescription("enter description");
        model.addAttribute(this.attributeName, attributeValue);
        return this.view;
    }

    @PostMapping
    String addProject(@ModelAttribute("ProjectWriteModel") ProjectWriteModel currentProject, Model model){
        this.projectService.save(currentProject);
        model.addAttribute(this.attributeName, new ProjectWriteModel());
        model.addAttribute("message", "New project has been added");
        return view;
    }

    @PostMapping(params = "addStep")
    String addProjectStep(@ModelAttribute("ProjectWriteModel") ProjectWriteModel currentProject){
        currentProject.getSteps().add(new ProjectStep());
        return this.view;
    }
}
