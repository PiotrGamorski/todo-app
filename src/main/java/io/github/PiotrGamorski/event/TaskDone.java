package io.github.PiotrGamorski.event;

import io.github.PiotrGamorski.model.Task;

import java.time.Clock;

public class TaskDone extends TaskEvent {
    TaskDone(Task source) {
        super(source.getId(), Clock.systemDefaultZone());
    }
}
