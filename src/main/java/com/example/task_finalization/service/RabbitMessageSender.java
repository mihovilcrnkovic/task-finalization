package com.example.task_finalization.service;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class RabbitMessageSender {

    final RabbitTemplate rabbitTemplate;
    final FanoutExchange processingJobExchange;
    final AuthenticationService authenticationService;

    public void sendMessage(Object message) {
        rabbitTemplate.convertAndSend(processingJobExchange.getName(), "", message, this::setMessageProperties);
    }

    private Message setMessageProperties(Message msg) {
        String token = authenticationService.getTokenFromContext();
        msg.getMessageProperties().setHeader("Authorization", "Bearer " + token);
        return msg;
    }
}
