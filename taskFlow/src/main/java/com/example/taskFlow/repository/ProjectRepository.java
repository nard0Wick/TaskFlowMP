package com.example.taskFlow.repository;

import com.example.taskFlow.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    Optional<Project> findByProjectName(String projectName);

    void deleteByProjectName(String projectName);

    boolean existsByProjectName(String projectName);
}
