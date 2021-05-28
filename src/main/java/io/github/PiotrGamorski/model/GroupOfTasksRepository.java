package io.github.PiotrGamorski.model;

import java.util.List;
import java.util.Optional;

public interface GroupOfTasksRepository {
    List<GroupOfTasks> findAll();
    Optional<GroupOfTasks> findById(Integer id);
    GroupOfTasks save(GroupOfTasks entity);
    boolean existsByDoneIsFalseAndProject_Id(Integer projectId);
    boolean existsByDescription(String description);
    List<GroupOfTasks> findAllByDescription(String description);
}
