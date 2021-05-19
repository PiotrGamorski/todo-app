package io.github.PiotrGamorski.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Entity
@Table(name = "task_groups")
public class GroupOfTasks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "Task_group's description must not be empty")
    private String description;
    private boolean done;
    @JsonManagedReference
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "group")
    private Set<Task> tasks;
    @ManyToOne
    @JoinColumn(name = "PROJECT_ID")
    private Project project;

    public GroupOfTasks() {}

    public GroupOfTasks(Project project){
        super();
        if(project != null){
            this.project = project;
        }
    }

    public int getId() { return id; }

    void setId(int id) { this.id = id; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public boolean isDone() { return done; }

    public void setDone(boolean done) { this.done = done; }

    public Set<Task> getTasks() { return tasks; }

    public void setTasks(Set<Task> tasks) { this.tasks = tasks; }

    public Project getProject() { return project; }

    public void setProject(Project project) { this.project = project; }
}
