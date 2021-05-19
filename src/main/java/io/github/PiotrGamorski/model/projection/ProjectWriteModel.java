package io.github.PiotrGamorski.model.projection;

import io.github.PiotrGamorski.model.Project;
import io.github.PiotrGamorski.model.ProjectStep;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.List;

public class ProjectWriteModel {
    @NotBlank(message = "Project's description must not be empty")
    private String description;
    @Valid
    private List<ProjectStep> steps;

    public Project toProject(){
        Project result = new Project();
        result.setDescription(this.description);
        this.steps.forEach(step -> step.setProject(result));
        result.setProjectSteps(new HashSet<>(this.steps));
        return result;
    }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public List<ProjectStep> getSteps() { return steps; }

    public void setSteps(List<ProjectStep> steps) { this.steps = steps; }
}
