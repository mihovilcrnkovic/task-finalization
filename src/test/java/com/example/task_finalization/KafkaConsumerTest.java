package com.example.task_finalization;

import com.example.task_finalization.model.ProcessingJob;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.ConfluentKafkaContainer;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@SpringBootTest
public class KafkaConsumerTest extends AbstractIntegrationTest {

    @Autowired
    KafkaTemplate<String, Object> kafkaTemplate;

    @Test
    void should_SendMessageToKafka() throws ExecutionException, InterruptedException {
        ProcessingJob processingJob = ProcessingJob.builder()
                .taskId(UUID.randomUUID())
                .result("Test")
                .processedAt(LocalDateTime.now())
                .id(UUID.randomUUID())
                .build();
        kafkaTemplate.send("my-topic", processingJob).get();
    }
}
