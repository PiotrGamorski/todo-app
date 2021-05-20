package io.github.PiotrGamorski.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "project_steps")
public class ProjectStep {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "Project step's description must not be empty")
    private String description;
    @NotNull
    private int daysToDeadline;
    @ManyToOne
    @JoinColumn(name = "PROJECT_ID")
    private Project project;

    public ProjectStep() {}

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public int getDaysToDeadline() { return daysToDeadline; }

    public void setDaysToDeadline(int daysToDeadline) { this.daysToDeadline = daysToDeadline; }

    public Project getProject() { return project; }

    public void setProject(Project project) { this.project = project; }
}
