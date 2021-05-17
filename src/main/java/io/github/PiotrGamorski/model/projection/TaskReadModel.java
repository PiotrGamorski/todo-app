package io.github.PiotrGamorski.model.projection;

import io.github.PiotrGamorski.model.Task;

public class TaskReadModel {
    private String description;
    private boolean done;

    public TaskReadModel(final Task source){
        description = source.getDescription();
        done = source.isDone();
    }

    public String getDescription() { return description;  }

    public void setDescription(String description) { this.description = description; }

    public boolean isDone() { return done; }

    public void setDone(boolean done) { this.done = done; }
}
