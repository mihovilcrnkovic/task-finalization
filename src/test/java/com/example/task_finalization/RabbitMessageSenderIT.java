package com.example.task_finalization;

import com.example.task_finalization.model.Finalization;
import com.example.task_finalization.service.RabbitMessageSender;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class RabbitMessageSenderIT extends AbstractIntegrationTest {

    @Autowired
    RabbitMessageSender rabbitMessageSender;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    Queue finalizationQueue;

    @Test
    void should_SendRabbitMessage() {
        Finalization finalization = Finalization.builder()
                .taskId(UUID.randomUUID())
                .finalizedAt(LocalDateTime.now())
                .outcome("Test")
                .build();

        rabbitMessageSender.sendMessage(finalization);

        Finalization result = (Finalization) rabbitTemplate.receiveAndConvert(finalizationQueue.getName());
        assertTrue(result.equals(finalization));
    }
}
