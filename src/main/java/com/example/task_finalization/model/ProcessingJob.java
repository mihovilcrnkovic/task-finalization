package com.example.task_finalization.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProcessingJob {
    UUID id;
    @NotNull
    UUID task_id;
    LocalDateTime receivedAt;
    LocalDateTime processedAt;
    @NotNull
    String result;
}
