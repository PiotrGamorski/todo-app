package io.github.PiotrGamorski;

import io.github.PiotrGamorski.model.GroupOfTasks;
import io.github.PiotrGamorski.model.GroupOfTasksRepository;
import io.github.PiotrGamorski.model.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class Warmup implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger logger = LoggerFactory.getLogger(Warmup.class);

    private final GroupOfTasksRepository groupOfTasksRepository;

    @Autowired
    Warmup(final GroupOfTasksRepository groupOfTasksRepository){
        this.groupOfTasksRepository = groupOfTasksRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        logger.info("Application warmup after context refreshed");
        final String description = "ApplicationContextEvent";
        if (!groupOfTasksRepository.existsByDescription(description)) {
            logger.info("No required group found! Adding it!");
            var group = new GroupOfTasks();
            group.setDescription(description);
            group.setTasks(Set.of(
                    new Task("ContextClosedEvent", null, group),
                    new Task("ContextRefreshedEvent", null, group),
                    new Task("ContextStoppedEvent", null, group),
                    new Task("ContextStartedEvent", null, group)
            ));
            groupOfTasksRepository.save(group);
        }
    }
}
