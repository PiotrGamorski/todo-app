package io.github.PiotrGamorski.controller;

import io.github.PiotrGamorski.model.Task;
import io.github.PiotrGamorski.model.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.time.LocalDateTime;
import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskControllerE2ETest {
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    TaskRepository taskRepository;

    @Test
    void httpGet_returnsAllTasks(){
        // given
        int initialTasksNumber = taskRepository.findAll().size();
        taskRepository.save(new Task("foo", LocalDateTime.now()));
        // when
        Task[] result = restTemplate.getForObject("http://localhost:" + port + "/tasks", Task[].class);
        // then
        assertThat(result).hasSize(initialTasksNumber + 1);
    }

    @Test
    void httpGet_returnsTaskById(){
        // given
        int id = taskRepository.save(new Task("foo", LocalDateTime.now())).getId();
        // when
        Task result = restTemplate.getForObject("http://localhost:" + port + "/tasks/" + id, Task.class);
        // then
        assertThat(result).isInstanceOf(Task.class);
        assertThat(result).hasFieldOrPropertyWithValue("id", id);
        assertThat(result).hasFieldOrPropertyWithValue("description", "foo");
    }

    @Test
    void httpPost_saveGivenTask(){
        // given
        var targetTask = new Task("foo", LocalDateTime.now());
        int id = restTemplate.postForObject("http://localhost:" + port + "/tasks", targetTask, Task.class).getId();
        // when
        Optional<Task> result = taskRepository.findById(id);
        // then
        assertThat(result).isNotEmpty();
        assertThat(result).get().hasFieldOrPropertyWithValue("id", id);
        assertThat(result).get().hasFieldOrPropertyWithValue("description", "foo");
    }

    @Test
    void httpPut_updatesGivenTask(){
        // given
        var targetTask = new Task("foo", LocalDateTime.now());
        int id = restTemplate.postForObject("http://localhost:" + port + "/tasks", targetTask, Task.class).getId();
        var sourceTask = new Task("bar", LocalDateTime.now());
        // when
        restTemplate.put("http://localhost:" + port + "/tasks/" + id, sourceTask);
        Optional<Task> result = taskRepository.findById(id);
        // then
        assertThat(result).isNotEmpty();
        assertThat(result).get().hasFieldOrPropertyWithValue("id", id);
        assertThat(result).get().hasFieldOrPropertyWithValue("description", "bar");
    }
}