package io.github.PiotrGamorski.adapter;

import io.github.PiotrGamorski.model.GroupOfTasks;
import io.github.PiotrGamorski.model.GroupOfTasksRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface SqlGroupOfTasksRepository extends GroupOfTasksRepository, JpaRepository<GroupOfTasks, Integer> {

    @Override
    @Query(value = "select distinct g from GroupOfTasks g join fetch g.tasks", nativeQuery = false)
    List<GroupOfTasks> findAll();

    @Override
    boolean existsByDoneIsFalseAndProject_Id(Integer projectId);
}
