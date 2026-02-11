package com.example.task_finalization.repository;

import com.example.task_finalization.model.Finalization;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FinalizationRepository extends JpaRepository<Finalization, UUID> {
    Optional<Finalization> findFinalizationByTaskId(@NotNull UUID taskId);
}
