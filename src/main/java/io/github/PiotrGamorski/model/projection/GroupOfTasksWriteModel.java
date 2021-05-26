package io.github.PiotrGamorski.model.projection;

import io.github.PiotrGamorski.model.GroupOfTasks;
import io.github.PiotrGamorski.model.Project;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GroupOfTasksWriteModel {
    @NotBlank(message = "Group's description cannot be empty")
    private String description;
    @Valid
    private List<TaskWriteModel> tasks = new ArrayList<TaskWriteModel>();

    public GroupOfTasksWriteModel(){
        tasks.add(new TaskWriteModel());
    }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public List<TaskWriteModel> getTasks() { return tasks; }

    public void setTasks(List<TaskWriteModel> tasks) { this.tasks = tasks; }

    public GroupOfTasks toGroup(final Project project){
        GroupOfTasks result = new GroupOfTasks(project);
        result.setDescription(this.description);
        result.setTasks(
                tasks.stream()
                        .map(taskWriteModel -> taskWriteModel.toTask(result))
                        .collect(Collectors.toSet())
        );
        return result;
    }
}
