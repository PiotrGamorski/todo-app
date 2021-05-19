package io.github.PiotrGamorski.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Entity
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "Project's description must not be empty")
    private String description;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "project")
    private Set<GroupOfTasks> taskGroups;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "project")
    private Set<ProjectStep> projectSteps;

    public Project() {}

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    Set<GroupOfTasks> getTaskGroups() { return taskGroups; }

    public void setTaskGroups(Set<GroupOfTasks> taskGroups) { this.taskGroups = taskGroups; }

    public Set<ProjectStep> getProjectSteps() { return projectSteps; }

    public void setProjectSteps(Set<ProjectStep> projectSteps) { this.projectSteps = projectSteps; }
}
