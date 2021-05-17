package io.github.PiotrGamorski.controller;

import io.github.PiotrGamorski.logic.TaskService;
import io.github.PiotrGamorski.model.Task;
import io.github.PiotrGamorski.model.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TaskControllerUnitTest {

    @Test
    @DisplayName("should update task when task exists")
    void updateTask_if_it_exists(){
        // given
        var inMemoryTaskRepository = inMemoryTaskRepository();
        var mockInMemoryTaskRepository = mock(inMemoryTaskRepository.getClass());
        when(mockInMemoryTaskRepository.existsById(anyInt())).thenReturn(true);
        // and
        var mockTaskService = mock(TaskService.class);
        // and
        var targetTask = new Task("foo", LocalDateTime.now());
        mockInMemoryTaskRepository.save(targetTask);
        int id = targetTask.getId();
        // system under test
        var toTest = new TaskController(mockInMemoryTaskRepository, mockTaskService);
        // when
        var result = toTest.updateTask(id, new Task("bar", LocalDateTime.now()));
        // then
        assertThat(result).isNotNull();
    }

    public InMemoryTaskRepository inMemoryTaskRepository(){
        return new InMemoryTaskRepository();
    }

    private static class InMemoryTaskRepository implements TaskRepository{
        private int index = 0;
        private final Map<Integer, Task> map = new HashMap<>();


        public int count(){
            return map.size();
        }

        @Override
        public List<Task> findAll() {
            return new ArrayList<>(map.values());
        }

        @Override
        public Optional<Task> findById(Integer id) {
            return Optional.ofNullable(map.get(id));
        }

        @Override
        public boolean existsById(Integer id) {
            return map.values().stream()
                    .anyMatch(task -> task.getId() == id);
        }

        @Override
        public boolean existsByDoneIsFalseAndGroupId(Integer groupId) {
            return map.values().stream()
                    .filter(task -> !task.isDone())
                    .anyMatch(task -> task.getGroup().getId() == groupId);
        }

        @Override
        public Task save(Task entity) {
            if(entity.getId() == 0){
                try {
                    var field = Task.class.getDeclaredField("id");
                    field.setAccessible(true);
                    field.set(entity, ++index);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
            map.put(entity.getId(), entity);
            return map.get(entity.getId());
        }

        @Override
        public Page<Task> findAll(Pageable page) {
            return null;
        }

        @Override
        public List<Task> findByDone(boolean done) {
            return map.values().stream()
                    .filter(Task::isDone)
                    .collect(Collectors.toList());
        }
    }
}
