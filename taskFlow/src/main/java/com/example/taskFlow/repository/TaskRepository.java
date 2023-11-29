package com.example.taskFlow.repository;

import com.example.taskFlow.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findByName(String taskName);

    void deleteByName(String taskName);

    boolean existsByName(String taskName);
}
