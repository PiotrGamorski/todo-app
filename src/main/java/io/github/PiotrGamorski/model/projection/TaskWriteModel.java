package io.github.PiotrGamorski.model.projection;

import io.github.PiotrGamorski.model.GroupOfTasks;
import io.github.PiotrGamorski.model.Task;

import java.time.LocalDateTime;

public class TaskWriteModel {
    private String description;
    private LocalDateTime deadline;

    public TaskWriteModel(){};

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getDeadline() { return deadline; }

    public void setDeadline(LocalDateTime deadline) { this.deadline = deadline; }

    public Task toTask(GroupOfTasks group) {
        return new Task(description, deadline, group);
    }
}
