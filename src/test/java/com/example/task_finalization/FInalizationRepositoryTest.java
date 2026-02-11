package com.example.task_finalization;

import com.example.task_finalization.model.Finalization;
import com.example.task_finalization.repository.FinalizationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class FInalizationRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    FinalizationRepository finalizationRepository;

    @Test
    void should_FindFinalizationByTaskId() {
        Finalization finalization = Finalization.builder()
                .taskId(UUID.randomUUID())
                .finalizedAt(LocalDateTime.now())
                .outcome("Test")
                .build();

        finalization = finalizationRepository.save(finalization);
        Optional<Finalization> result = finalizationRepository.findFinalizationByTaskId(finalization.getTaskId());

        assertTrue(result.isPresent());
    }
}
