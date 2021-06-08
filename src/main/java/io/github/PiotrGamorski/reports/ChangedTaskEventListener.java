package io.github.PiotrGamorski.reports;

import io.github.PiotrGamorski.event.TaskDone;
import io.github.PiotrGamorski.event.TaskEvent;
import io.github.PiotrGamorski.event.TaskUndone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ChangedTaskEventListener {
    private static final Logger logg = LoggerFactory.getLogger(ChangedTaskEventListener.class);

    private final PersistedTaskEventRepository repository;

    @Autowired
    ChangedTaskEventListener(final PersistedTaskEventRepository repository){
        this.repository = repository;
    }

    @Async
    @EventListener
    public void on(TaskDone event){
        onChanged(event);
    }

    @Async
    @EventListener
    public void on(TaskUndone event){
        onChanged(event);
    }

    private void onChanged(final TaskEvent event){
        logg.info("Got " + event.toString());
        repository.save(new PersistedTaskEvent(event));
    }
}
