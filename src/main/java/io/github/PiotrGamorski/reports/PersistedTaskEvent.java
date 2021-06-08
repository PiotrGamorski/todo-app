package io.github.PiotrGamorski.reports;

import io.github.PiotrGamorski.event.TaskEvent;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
@Table(name = "TASK_EVENTS")
class PersistedTaskEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    int taskId;
    String name;
    LocalDateTime occurrence;

    public PersistedTaskEvent(){}
    public PersistedTaskEvent(TaskEvent source){
        taskId = source.getTaskId();
        name = source.getClass().getSimpleName();
        occurrence = LocalDateTime.ofInstant(source.getOccurrence(), ZoneId.systemDefault());
    }

    public LocalDateTime getOccurrence() {
        return occurrence;
    }
}
