package io.github.PiotrGamorski.logic;

import io.github.PiotrGamorski.TaskConfigurationProperties;
import io.github.PiotrGamorski.model.*;
import io.github.PiotrGamorski.model.projection.GroupOfTasksReadModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProjectServiceTest {

    @Test
    @DisplayName("should throw IllegalStateException when configured to allow just 1 group and undone group exists")
    void createGroup_noMultipleGroupsConfig_And_undoneGroupExists_throwsIllegalStateException() {
        // given
        GroupOfTasksRepository mockGroupRepository = groupRepositoryReturning(true);
        // and
        TaskConfigurationProperties mockConfig = configurationReturning(false);
        // system under test
        var toTest = new ProjectService(null, mockGroupRepository, null, mockConfig);

        // when
        var exception = catchThrowable(()-> toTest.createGroup(LocalDateTime.now(), 0));
        // then
        assertThat(exception)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("one undone group");
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when configuration ok and no projects for a given id")
    void createGroup_configurationOk_And_noProjects_throwsIllegalArgumentException() {
        // given
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());
        // and
        var mockConfig = configurationReturning(true);
        // system under test
        var toTest = new ProjectService(mockRepository, null, null, mockConfig);

        // when
        var exception = catchThrowable(()-> toTest.createGroup(LocalDateTime.now(), 0));
        // then
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("id not found");
        }

    @Test
    @DisplayName("should throw IllegalArgumentException when configured to allow just 1 group and no groups and projects")
    void createGroup_noMultipleGroupsConfig_And_noUndoneGroupExists_noProjects_throwsIllegalArgumentException() {
        // given
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());
        // and
        GroupOfTasksRepository mockGroupRepository = groupRepositoryReturning(false);
        // and
        TaskConfigurationProperties mockConfig = configurationReturning(true);
        // system under test
        var toTest = new ProjectService(mockRepository, mockGroupRepository, null, mockConfig);

        // when
        var exception = catchThrowable(()-> toTest.createGroup(LocalDateTime.now(), 0));
        // then
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("id not found");
    }

    @Test
    @DisplayName("should create a new group from project")
    void createGroup_configurationOk_existingProject_createsAndSavesGroup(){
        //given
        var today = LocalDate.now().atStartOfDay();
        // and
        var project = projectWith("test", Set.of(-1, -2));
        // and
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt()))
                .thenReturn(Optional.of(project));
        // and
        InMemoryGroupRepository inMemoryGroupRepository = inMemoryGroupRepository();
        var groupOfTasksService = groupOfTasksServiceWithNullTaskRepo(inMemoryGroupRepository);
        // and
        int  countBeforeCall = inMemoryGroupRepository.count();
        // and
        TaskConfigurationProperties mockConfig = configurationReturning(true);
        // system under test
        var toTest = new ProjectService(mockRepository, inMemoryGroupRepository, groupOfTasksService, mockConfig);

        // when
        GroupOfTasksReadModel result = toTest.createGroup(today, 1);

        // then
        assertThat(result.getDescription()).isEqualTo("test");
        assertThat(result.getDeadline()).isEqualTo(today.minusDays(1));
        assertThat(result.getTasks().stream().allMatch(task -> task.getDescription().equals("foo")));
        assertThat(countBeforeCall + 1).isEqualTo(inMemoryGroupRepository.count());
    }

    private GroupOfTasksService groupOfTasksServiceWithNullTaskRepo(final InMemoryGroupRepository inMemoryGroupRepository){
        return new GroupOfTasksService(inMemoryGroupRepository, null);
    }

    private Project projectWith(String projectDescription, Set<Integer> daysToDeadline) {
        Set<ProjectStep>steps = daysToDeadline.stream()
                .map(days -> {
                    var step = mock(ProjectStep.class);
                    when(step.getDescription()).thenReturn("foo");
                    when(step.getDaysToDeadline()).thenReturn(days);
                    return step;
                })
                .collect(Collectors.toSet());
        var result = mock(Project.class);
        when(result.getDescription()).thenReturn(projectDescription);
        when(result.getProjectSteps()).thenReturn(steps);
        return result;
    }

    private GroupOfTasksRepository groupRepositoryReturning(final boolean result){
        var mockGroupRepository = mock(GroupOfTasksRepository.class);
        when(mockGroupRepository.existsByDoneIsFalseAndProject_Id(anyInt())).thenReturn(result);
        return mockGroupRepository;
    }

    private TaskConfigurationProperties configurationReturning(final boolean result){
        var mockTemplate = mock(TaskConfigurationProperties.Template.class);
        when(mockTemplate.isAllowMultipleTasksFromTemplate()).thenReturn(result);
        var mockConfig = mock(TaskConfigurationProperties.class);
        when(mockConfig.getTemplate()).thenReturn(mockTemplate);
        return mockConfig;
    }

    private InMemoryGroupRepository inMemoryGroupRepository(){
        return new InMemoryGroupRepository();
    }

    private static class InMemoryGroupRepository implements GroupOfTasksRepository{
        private int index = 0;
        private final Map<Integer, GroupOfTasks> map = new HashMap<>();

        public int count() {
            return map.values().size();
        }

        @Override
        public List<GroupOfTasks> findAll() {
            return new ArrayList<>(map.values());
        }

        @Override
        public Optional<GroupOfTasks> findById(Integer id) {
            return Optional.ofNullable(map.get(id));
        }

        @Override
        public GroupOfTasks save(GroupOfTasks entity) {
            if(entity.getId() == 0){
                try {
                    var field = GroupOfTasks.class.getDeclaredField("id");
                    field.setAccessible(true);
                    field.set(entity, ++index);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
            map.put(entity.getId(), entity);
            return entity;
        }

        @Override
        public boolean existsByDoneIsFalseAndProject_Id(Integer projectId) {
            return map.values().stream()
                    .filter(groupOfTasks -> !groupOfTasks.isDone())
                    .anyMatch(groupOfTasks -> groupOfTasks.getProject() != null && groupOfTasks.getProject().getId() == projectId);
        }
    }
}