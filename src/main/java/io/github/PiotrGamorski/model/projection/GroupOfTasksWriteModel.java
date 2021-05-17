package io.github.PiotrGamorski.model.projection;

import io.github.PiotrGamorski.model.GroupOfTasks;

import java.util.Set;
import java.util.stream.Collectors;

public class GroupOfTasksWriteModel {
    private String description;
    private Set<TaskWriteModel> tasks;

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public Set<TaskWriteModel> getTasks() { return tasks; }

    public void setTasks(Set<TaskWriteModel> tasks) { this.tasks = tasks; }

    public GroupOfTasks toGroup(){
        GroupOfTasks result = new GroupOfTasks();
        result.setDescription(this.description);
        result.setTasks(
                tasks.stream()
                        .map(taskWriteModel -> taskWriteModel.toTask(result))
                        .collect(Collectors.toSet())
        );
        return result;
    }
}
