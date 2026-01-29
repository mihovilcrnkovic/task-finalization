package com.example.task_finalization.repository;

import com.example.task_finalization.model.FinalizationEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FinalizationEventRepository extends JpaRepository<FinalizationEvent, Long> {
}
