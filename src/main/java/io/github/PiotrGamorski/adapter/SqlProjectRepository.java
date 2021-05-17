package io.github.PiotrGamorski.adapter;

import io.github.PiotrGamorski.model.Project;
import io.github.PiotrGamorski.model.ProjectRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface SqlProjectRepository extends ProjectRepository, JpaRepository<Project, Integer> {

    @Override
    @Query(value = "select distinct p from Project p join fetch p.projectSteps", nativeQuery = false)
    List<Project> findAll();
}
