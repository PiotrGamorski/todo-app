package io.github.PiotrGamorski.adapter;

import io.github.PiotrGamorski.model.Task;
import io.github.PiotrGamorski.model.TaskRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
interface SqlTaskRepository extends TaskRepository, JpaRepository<Task, Integer> {
    @Override
    @Query(nativeQuery = true, value = "SELECT count(*)>0 FROM TASKS WHERE id=:id")
    boolean existsById(@Param("id") Integer id);

    @Override
    boolean existsByDoneIsFalseAndGroupId(Integer groupId);

    @Override
    @Query(nativeQuery = true, value = "SELECT * FROM TASKS WHERE TASK_GROUP_ID=:groupId")
    List<Task> findAllByGroup_Id(@Param("groupId") Integer groupId);

    @Override
    @Query(nativeQuery = true, value = "SELECT * FROM TASKS WHERE DONE = FALSE AND DEADLINE <=:today")
    List<Task> findAllUndoneByDeadline(@Param("today") LocalDate deadline);
}
