package io.github.PiotrGamorski.logic;

import io.github.PiotrGamorski.TaskConfigurationProperties;
import io.github.PiotrGamorski.model.GroupOfTasksRepository;
import io.github.PiotrGamorski.model.ProjectRepository;
import io.github.PiotrGamorski.model.TaskRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class LogicConfiguration {
    @Bean
    ProjectService projectService(
            final ProjectRepository repository,
            final GroupOfTasksRepository groupOfTasksRepository,
            final GroupOfTasksService groupOfTasksService,
            final TaskConfigurationProperties config
            ){
        return new ProjectService(repository, groupOfTasksRepository, groupOfTasksService, config);
    }

    @Bean
    GroupOfTasksService groupOfTasksService(
            final GroupOfTasksRepository repository,
            final TaskRepository taskRepository
            ){
        return new GroupOfTasksService(repository, taskRepository);
    }

    @Bean
    TaskService taskService(final TaskRepository taskRepository){
        return new TaskService(taskRepository);
    }
}
