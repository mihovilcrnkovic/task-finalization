package com.example.task_finalization;

import com.example.task_finalization.model.ProcessingJob;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.protocol.types.Field;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class KafkaConsumerTest extends AbstractIntegrationTest {

    @Autowired
    KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    ConsumerFactory<String, Object> consumerFactory;

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
        kafkaTemplate.send("my-topic", processingJob).get();

        ConsumerRecords<String, Object> records = KafkaTestUtils.getRecords(testConsumer);
        assertTrue(!records.isEmpty());
    }
}
