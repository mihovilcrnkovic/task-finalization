package com.example.task_finalization;

import com.example.task_finalization.model.Finalization;
import com.example.task_finalization.model.ProcessingJob;
import com.example.task_finalization.repository.FinalizationRepository;
import com.example.task_finalization.service.FinalizationService;
import com.example.task_finalization.service.RabbitMessageSender;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FinalizationServiceTest {

    @Mock
    RabbitMessageSender rabbitMessageSender;

    @Mock
    FinalizationRepository finalizationRepository;

    @InjectMocks
    FinalizationService finalizationService;

    @Test
    void should_CreateFinalization_when_JobFinalized() {
        ProcessingJob job = ProcessingJob.builder()
                .id(UUID.randomUUID())
                .result("Test")
                .taskId(UUID.randomUUID())
                .processedAt(LocalDateTime.now())
                .receivedAt(LocalDateTime.now())
                .build();

        Finalization finalization = Finalization.builder()
                .taskId(job.getTaskId())
                .finalizedAt(LocalDateTime.now())
                .outcome("Success!")
                .build();

        when(finalizationRepository.save(any(Finalization.class))).thenReturn(finalization);

        Finalization result = finalizationService.finalizeJob(job);
        assertTrue(result.equals(finalization));
    }

    /* @Test
    void testFail() {
        assertTrue(false);
    }*/
}
