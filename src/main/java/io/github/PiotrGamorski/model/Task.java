package io.github.PiotrGamorski.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "Task's description must not be empty")
    private String description;
    private boolean done;
    private LocalDateTime deadline;
    @Embedded
    private final Audit audit = new Audit();
    @ManyToOne
    @JoinColumn(name = "TASK_GROUP_ID")
    private GroupOfTasks group;

    public Task() {}

    public Task(String description, LocalDateTime deadline){
        this(description, deadline, null);
    }

    public Task(String description, LocalDateTime deadline, GroupOfTasks group){
        this.description = description;
        this.deadline = deadline;
        if(group != null){
            this.group = group;
        }
    }

    public int getId() { return id; }

    void setId(int id) { this.id = id; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public boolean isDone() { return done; }

    public void setDone(boolean done) { this.done = done; }

    public LocalDateTime getDeadline() { return deadline; }

    public void setDeadline(LocalDateTime deadline) { this.deadline = deadline; }

    public GroupOfTasks getGroup() { return group; }

    void setGroup(GroupOfTasks group) { this.group = group; }

    public void updateFrom(final Task source) {
        this.description = source.description;
        this.done = source.done;
        this.deadline = source.deadline;
        this.group = source.group;
    }
}
