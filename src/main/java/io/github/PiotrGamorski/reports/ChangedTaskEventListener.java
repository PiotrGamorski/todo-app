package io.github.PiotrGamorski.reports;

import io.github.PiotrGamorski.event.TaskDone;
import io.github.PiotrGamorski.event.TaskUndone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class ChangedTaskEventListener {
    private static final Logger logg = LoggerFactory.getLogger(ChangedTaskEventListener.class);

    @EventListener
    public void on(TaskDone event){
        logg.info("Got " + event.toString());
    }

    @EventListener
    public void on(TaskUndone event){
        logg.info("Got" + event.toString());
    }
}
