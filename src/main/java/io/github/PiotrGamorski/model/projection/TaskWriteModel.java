package io.github.PiotrGamorski.model.projection;

import io.github.PiotrGamorski.model.GroupOfTasks;
import io.github.PiotrGamorski.model.Task;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class TaskWriteModel {
    @NotBlank(message = "Task's description cannot be empty")
    private String description;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
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
