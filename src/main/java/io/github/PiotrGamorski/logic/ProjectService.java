package io.github.PiotrGamorski.logic;

import io.github.PiotrGamorski.TaskConfigurationProperties;
import io.github.PiotrGamorski.model.GroupOfTasksRepository;
import io.github.PiotrGamorski.model.Project;
import io.github.PiotrGamorski.model.ProjectRepository;
import io.github.PiotrGamorski.model.projection.GroupOfTasksReadModel;
import io.github.PiotrGamorski.model.projection.GroupOfTasksWriteModel;
import io.github.PiotrGamorski.model.projection.ProjectWriteModel;
import io.github.PiotrGamorski.model.projection.TaskWriteModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectService {
    private final ProjectRepository repository;
    private final GroupOfTasksRepository groupOfTasksRepository;
    private final GroupOfTasksService groupOfTasksService;
    private final TaskConfigurationProperties config;

    public ProjectService(final ProjectRepository repository, final GroupOfTasksRepository groupOfTasksRepository,  final GroupOfTasksService groupOfTasksService, final TaskConfigurationProperties config){
        this.repository = repository;
        this.groupOfTasksRepository = groupOfTasksRepository;
        this.groupOfTasksService = groupOfTasksService;
        this.config = config;
    }

    public List<Project> readAll() {
        return repository.findAll();
    }

    public Project save(ProjectWriteModel toSave){
        return repository.save(toSave.toProject());
    }

    public GroupOfTasksReadModel createGroup(LocalDateTime deadline, int projectId){
        if(!config.getTemplate().isAllowMultipleTasksFromTemplate() && groupOfTasksRepository.existsByDoneIsFalseAndProject_Id(projectId)){
            throw new IllegalStateException("Only one undone group from project is allowed");
        }

        return repository.findById(projectId)
                .map(project -> {
                    var targetGroup = new GroupOfTasksWriteModel();
                    targetGroup.setDescription(project.getDescription());
                    targetGroup.setTasks(
                            project.getProjectSteps().stream()
                            .map(projectStep -> {
                                var task = new TaskWriteModel();
                                task.setDescription(project.getDescription());
                                task.setDeadline(deadline.plusDays(projectStep.getDaysToDeadline()));
                                return task;
                            }
                            ).collect(Collectors.toSet())
                    );
                    return groupOfTasksService.createGroup(targetGroup, project);
                }).orElseThrow(()-> new IllegalArgumentException("Project with given id not found"));
    }
}
