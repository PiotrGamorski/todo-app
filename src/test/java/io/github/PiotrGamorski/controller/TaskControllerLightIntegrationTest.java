package io.github.PiotrGamorski.controller;

import io.github.PiotrGamorski.model.Task;
import io.github.PiotrGamorski.model.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@WebMvcTest(TaskController.class)
@ActiveProfiles("integration")
class TaskControllerLightIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    TaskRepository taskRepository;

    @Test
    void httpGet_returnsGivenTask(){
        // given
        when(taskRepository.findById(anyInt()))
                .thenReturn(Optional.of(new Task("foo", LocalDateTime.now())));
        // when + then
        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/tasks/47"))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.content().string(containsString("\"description\":\"foo\"")));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
