package com.example.task_finalization.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class FinalizationEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "finalization_id")
    Finalization finalization;
    @NotNull
    String eventType;
    LocalDateTime createdAt;
}
