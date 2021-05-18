package io.github.PiotrGamorski.logic;

import io.github.PiotrGamorski.model.GroupOfTasks;
import io.github.PiotrGamorski.model.GroupOfTasksRepository;
import io.github.PiotrGamorski.model.TaskRepository;
import io.github.PiotrGamorski.model.projection.GroupOfTasksReadModel;
import io.github.PiotrGamorski.model.projection.GroupOfTasksWriteModel;

import java.util.List;
import java.util.stream.Collectors;

public class GroupOfTasksService {
    private final GroupOfTasksRepository repository;
    private final TaskRepository taskRepository;

    GroupOfTasksService(final GroupOfTasksRepository repository, final TaskRepository taskRepository){
        this.repository = repository;
        this.taskRepository = taskRepository;
    }

    public GroupOfTasksReadModel createGroup(GroupOfTasksWriteModel source){
        GroupOfTasks result = repository.save(source.toGroup());
        return new GroupOfTasksReadModel(result);
    }

    public List<GroupOfTasksReadModel> readAll(){
        return repository.findAll().stream()
                .map(GroupOfTasksReadModel::new)
                .collect(Collectors.toList());
    }

    public void toggleDonePropInGroup(int groupId){
        if(taskRepository.existsByDoneIsFalseAndGroupId(groupId)){
            throw new IllegalStateException("Group has undone tasks. Finish all the tasks first.");
        }

        GroupOfTasks result = repository.findById(groupId)
                .orElseThrow(()-> new IllegalArgumentException("Group with the given id not found"));
        result.setDone(!result.isDone());
        repository.save(result);
    }
}
