package com.example.task_finalization;

import com.example.task_finalization.model.Finalization;
import com.example.task_finalization.model.ProcessingJob;
import com.example.task_finalization.repository.FinalizationRepository;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.testcontainers.shaded.org.awaitility.Awaitility;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class KafkaConsumerTest extends AbstractIntegrationTest {

    @Autowired
    KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    ConsumerFactory<String, Object> consumerFactory;

    @Autowired
    FinalizationRepository finalizationRepository;

    private Consumer<String, Object> testConsumer;

    @BeforeEach
    void createKafkaConsumer() {
        testConsumer = consumerFactory.createConsumer(UUID.randomUUID().toString(), "test");
        testConsumer.subscribe(List.of("my-topic"));
    }

    @Test
    void should_SendMessageToKafka() throws ExecutionException, InterruptedException {
        ProcessingJob processingJob = ProcessingJob.builder()
                .taskId(UUID.randomUUID())
                .result("Test")
                .processedAt(LocalDateTime.now())
                .id(UUID.randomUUID())
                .build();

        Message<ProcessingJob> msg = MessageBuilder.withPayload(processingJob)
                .setHeader(KafkaHeaders.TOPIC, "my-topic")
                .setHeader("Authorization", "Bearer " + token)
                .build();
        kafkaTemplate.send(msg).get();


        ConsumerRecords<String, Object> records = KafkaTestUtils.getRecords(testConsumer);

        assertTrue(!records.isEmpty());

        Awaitility.await()
                .atMost(Duration.of(5, ChronoUnit.SECONDS))
                .untilAsserted(() -> {
                    Optional<Finalization> optionalFinalization = finalizationRepository.findFinalizationByTaskId(processingJob.getTaskId());
                    assertTrue(optionalFinalization.isPresent());
                });
        ;
    }
}
