package com.example.task_finalization.service;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class RabbitMessageSender {

    final RabbitTemplate rabbitTemplate;
    final FanoutExchange processingJobExchange;

    public void sendMessage() {
        String message = "Hello world!";
        rabbitTemplate.convertAndSend(processingJobExchange.getName(), message);
    }

    @EventListener(ApplicationReadyEvent.class)
    void test(){
        sendMessage();
    }
}
