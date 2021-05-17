package io.github.PiotrGamorski.logic;

import io.github.PiotrGamorski.model.GroupOfTasks;
import io.github.PiotrGamorski.model.GroupOfTasksRepository;
import io.github.PiotrGamorski.model.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GroupOfTasksServiceTest {
    @Test
    @DisplayName("should throw when undone tasks")
    void toggleGroup_undoneTasks_throwsIllegalStateException() {
        //given
        var mockTaskRepository = this.taskRepositoryExistsByDoneReturning(true);
        // system under test
        var toTest = new GroupOfTasksService(null, mockTaskRepository);
        // when
        var exception = catchThrowable(()-> toTest.toggleDonePropInGroup(0));
        // then
        assertThat(exception).isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("undone tasks");
    }

    @Test
    @DisplayName("should throw when no group")
    void toggleGroup_wrongId_throwsIllegalArgumentException () {
        //given
        var mockTaskRepository = this.taskRepositoryExistsByDoneReturning(false);
        // and
        var mockGroupOfTasksRepository = mock(GroupOfTasksRepository.class);
        when(mockGroupOfTasksRepository.findById(anyInt())).thenReturn(Optional.empty());
        // system under test
        var toTest = new GroupOfTasksService(mockGroupOfTasksRepository, mockTaskRepository);
        // when
        var exception = catchThrowable(()->toTest.toggleDonePropInGroup(0));
        // then
        assertThat(exception).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("id not found");
    }

    @Test
    @DisplayName("should toggle group")
    void toggleGroup_worksAsExpected() {
        //given
        var mockTaskRepository = this.taskRepositoryExistsByDoneReturning(false);
        // and
        var group = new GroupOfTasks();
        var beforeToggle  = group.isDone();
        // and
        var mockGroupOfTasksRepository = mock(GroupOfTasksRepository.class);
        when(mockGroupOfTasksRepository.findById(anyInt())).thenReturn(Optional.of(group));
        // system under test
        var toTest = new GroupOfTasksService(mockGroupOfTasksRepository, mockTaskRepository);
        // when
        toTest.toggleDonePropInGroup(0);
        // then
        assertThat(group.isDone()).isNotEqualTo(beforeToggle);
    }

    @Test
    @DisplayName("should save GroupOfTasks")
    void save_groupOfTasks_ok(){
        // given
        var mockTaskRepository = this.taskRepositoryExistsByDoneReturning(false);
        // and
        var inMemoryGroupOfTasksRepository = this.inMemoryGroupOfTasksRepository();
        // and
        var countBeforeCall = inMemoryGroupOfTasksRepository.count();
        // system under test
        var toTest = new GroupOfTasksService(inMemoryGroupOfTasksRepository, mockTaskRepository);
        // when
        toTest.toggleDonePropInGroup(0);
        // then
        assertThat(countBeforeCall + 1).isEqualTo(inMemoryGroupOfTasksRepository.count());
    }

    private TaskRepository taskRepositoryExistsByDoneReturning(final boolean result){
        var mockTaskRepository = mock(TaskRepository.class);
        when(mockTaskRepository.existsByDoneIsFalseAndGroupId(anyInt())).thenReturn(result);
        return mockTaskRepository;
    }

    private InMemoryGroupOfTasksRepository inMemoryGroupOfTasksRepository(){
        return new InMemoryGroupOfTasksRepository();
    }

    private static class InMemoryGroupOfTasksRepository implements GroupOfTasksRepository{
        private int index = 0;
        private final Map<Integer, GroupOfTasks> map = new HashMap<>();
        private final GroupOfTasks group = new GroupOfTasks();

        public int count() {
            return map.values().size();
        }

        @Override
        public List<GroupOfTasks> findAll() {
            return new ArrayList<>(map.values());
        }

        @Override
        public Optional<GroupOfTasks> findById(Integer id) {
            return Optional.of(group);
        }

        @Override
        public GroupOfTasks save(GroupOfTasks entity) {
            if(entity.getId() == 0){
                try{
                    var field = GroupOfTasks.class.getDeclaredField("id");
                    field.setAccessible(true);
                    field.set(entity, ++index);
                }
                catch (NoSuchFieldException | IllegalAccessException e) {
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
                    .anyMatch(groupOfTasks -> groupOfTasks.getProject().getId() == projectId);
        }
    }
}