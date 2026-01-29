package com.example.task_finalization.repository;

import com.example.task_finalization.model.Finalization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FinalizationRepository extends JpaRepository<Finalization, UUID> {
}
