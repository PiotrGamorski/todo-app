package io.github.PiotrGamorski.event;

import io.github.PiotrGamorski.model.Task;

import java.time.Clock;

public class TaskUndone extends TaskEvent {
    TaskUndone(Task source) {
        super(source.getId(), Clock.systemDefaultZone());
    }
}
