package io.github.PiotrGamorski.reports;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PersistedTaskEventRepository extends JpaRepository<PersistedTaskEvent, Integer> {
    List<PersistedTaskEvent> findByTaskId(int taskId);
    List<PersistedTaskEvent> findAll();

    @Query(nativeQuery = true, value = "SELECT DISTINCT TASK_EVENTS.ID, TASK_EVENTS.TASK_ID, TASK_EVENTS.NAME, TASK_EVENTS.OCCURRENCE" +
            " FROM TASKS " +
            "RIGHT JOIN" +
            " TASK_EVENTS ON TASK_EVENTS.TASK_ID=TASKS.ID" +
            " WHERE DEADLINE IS NOT NULL AND DEADLINE <= OCCURRENCE AND NAME='TaskDone' AND TASKS.DONE=TRUE")
    List<PersistedTaskEvent> findAllDoneBeforeDeadline();
}
