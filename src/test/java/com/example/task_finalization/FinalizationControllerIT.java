package com.example.task_finalization;

import com.example.task_finalization.model.ProcessingJob;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@SpringBootTest
@AutoConfigureWebTestClient
public class FinalizationControllerIT extends AbstractIntegrationTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    void should_FinalizeProcessingJob() {
        ProcessingJob processingJob = ProcessingJob.builder()
                .id(UUID.randomUUID())
                .taskId(UUID.randomUUID())
                .receivedAt(LocalDateTime.now())
                .processedAt(LocalDateTime.now().plus(2, ChronoUnit.SECONDS))
                .result("Processed!")
                .build();

        webTestClient.post().uri("/api/finalize")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(processingJob)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.taskId").isEqualTo(processingJob.getTaskId())
                .jsonPath("$.outcome").isEqualTo("Success!");

    }

    @Test
    void should_ReturnBadRequest_when_ResultIsNull() {
        ProcessingJob processingJob = ProcessingJob.builder()
                .id(UUID.randomUUID())
                .taskId(UUID.randomUUID())
                .receivedAt(LocalDateTime.now())
                .processedAt(LocalDateTime.now().plus(2, ChronoUnit.SECONDS))
                .build();

        webTestClient.post().uri("/api/finalize")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(processingJob)
                .exchange()
                .expectStatus().isBadRequest();
    }
}
