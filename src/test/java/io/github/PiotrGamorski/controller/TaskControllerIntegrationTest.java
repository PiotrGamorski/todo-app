package io.github.PiotrGamorski.controller;

import io.github.PiotrGamorski.model.Task;
import io.github.PiotrGamorski.model.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.containsString;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integration")
class TaskControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    TaskRepository taskRepository;

    @Test
    void httpGet_returnsAllTasks(){
        // given
        taskRepository.save(new Task("foo", LocalDateTime.now()));
        taskRepository.save(new Task("bar", LocalDateTime.now()));
        // when + then
        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/tasks"))
                    .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                    .andExpect(MockMvcResultMatchers.content().string(containsString("\"description\":\"foo\"")))
                    .andExpect(MockMvcResultMatchers.content().string(containsString("\"description\":\"bar\"")));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void httpGet_returnsGivenTask(){
        // given
        int id = taskRepository.save(new Task("foo", LocalDateTime.now())).getId();
        // when + then
        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/tasks/" + id))
                    .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                    .andExpect(MockMvcResultMatchers.content().string(containsString("\"description\":\"foo\"")));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
