package com.example.taskFlow.repository;

import com.example.taskFlow.model.Side;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SideRepository extends JpaRepository<Side, Long> {
}
