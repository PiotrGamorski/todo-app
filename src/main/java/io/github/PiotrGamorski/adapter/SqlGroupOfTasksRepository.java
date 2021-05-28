package io.github.PiotrGamorski.adapter;

import io.github.PiotrGamorski.model.GroupOfTasks;
import io.github.PiotrGamorski.model.GroupOfTasksRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface SqlGroupOfTasksRepository extends GroupOfTasksRepository, JpaRepository<GroupOfTasks, Integer> {

    @Override
    @Query(nativeQuery = false, value = "select distinct g from GroupOfTasks g join fetch g.tasks")
    List<GroupOfTasks> findAll();

    @Override
    @Query(nativeQuery = true, value = "SELECT * FROM TASK_GROUPS WHERE description:=description")
    List<GroupOfTasks> findAllByDescription(@Param("description") String description);
}
