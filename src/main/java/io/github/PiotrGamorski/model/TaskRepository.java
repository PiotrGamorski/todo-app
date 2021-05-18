package io.github.PiotrGamorski.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    List<Task> findAll();
    Optional<Task> findById(Integer id);
    boolean existsById(Integer id);
    boolean existsByDoneIsFalseAndGroupId(Integer groupId);
    Task save(Task entity);
    Page<Task> findAll(Pageable page);
    List<Task> findByDone(boolean done);
    List<Task> findAllByGroup_Id(Integer groupId);
    List<Task> findAllUndoneByDeadline(LocalDate deadline);
}
