package io.github.PiotrGamorski.model.projection;

import io.github.PiotrGamorski.model.Task;
import io.github.PiotrGamorski.model.GroupOfTasks;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public class GroupOfTasksReadModel {
    private String description;
    private LocalDateTime deadline;
    private Set<TaskReadModel> tasks;

    public GroupOfTasksReadModel(final GroupOfTasks source){
        this.description = source.getDescription();
        source.getTasks().stream()
                .map(Task::getDeadline)
                .max(LocalDateTime::compareTo)
                .ifPresent(this::setDeadline);
        this.tasks = source.getTasks().stream()
                .map(TaskReadModel::new)
                .collect(Collectors.toSet());
    }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getDeadline() { return deadline; }

    public void setDeadline(LocalDateTime deadline) { this.deadline = deadline; }

    public Set<TaskReadModel> getTasks() { return tasks; }

    public void setTasks(Set<TaskReadModel> tasks) { this.tasks = tasks; }
}
