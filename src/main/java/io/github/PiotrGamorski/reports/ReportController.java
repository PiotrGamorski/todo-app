package io.github.PiotrGamorski.reports;

import io.github.PiotrGamorski.model.Task;
import io.github.PiotrGamorski.model.TaskRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportController {
    private final TaskRepository taskRepository;
    private final PersistedTaskEventRepository persistedTaskEventRepository;

    public ReportController(TaskRepository taskRepository, PersistedTaskEventRepository persistedTaskEventRepository) {
        this.taskRepository = taskRepository;
        this.persistedTaskEventRepository = persistedTaskEventRepository;
    }

    @GetMapping("/count/{id}")
    ResponseEntity<TaskWithChangesCounted> readTaskWithChangesCounted(@PathVariable int id){
        return taskRepository.findById(id)
                .map(task -> new TaskWithChangesCounted(task, persistedTaskEventRepository.findByTaskId(id)))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    private static class TaskWithChangesCounted {
        // if fields are public, then they are serialized and could be seen in JSON response.
        public String description;
        public boolean done;
        public int changesCounter;

        TaskWithChangesCounted(final Task task, final List<PersistedTaskEvent> events){
            this.description = task.getDescription();
            this.done = task.isDone();
            this.changesCounter = events.size();
        }
    }

    @GetMapping("/deadline/{id}")
    ResponseEntity<TaskDoneBeforeDeadline> readTaskBeforeDeadline(@PathVariable int id){

       return taskRepository.findById(id)
                .map(task -> new TaskDoneBeforeDeadline(task, persistedTaskEventRepository.findByTaskId(id)))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    private static class TaskDoneBeforeDeadline {
        public String description;
        public boolean doneBeforeDeadline;

        TaskDoneBeforeDeadline(final Task task, final List<PersistedTaskEvent> events){
            this.description = task.getDescription();
            boolean done = task.isDone();
            LocalDateTime deadline = task.getDeadline();
            LocalDateTime dateOfLastChange = events.stream()
                    .max(Comparator.comparing(PersistedTaskEvent::getOccurrence)).get()
                    .getOccurrence();

            this.doneBeforeDeadline = deadline.isBefore(dateOfLastChange) && done;
        }
    }
}
