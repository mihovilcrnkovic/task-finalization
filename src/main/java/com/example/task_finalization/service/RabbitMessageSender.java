package com.example.task_finalization.service;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;


@Component
@RequiredArgsConstructor
public class RabbitMessageSender {

    final RabbitTemplate rabbitTemplate;
    final FanoutExchange processingJobExchange;
    final AuthenticationService authenticationService;

    public void sendMessage(Object message) {
        String bearerToken = "Bearer " + authenticationService.getTokenFromContext();
        rabbitTemplate.convertAndSend(processingJobExchange.getName(), "", message, (msg) -> {
            msg.getMessageProperties().setHeader("Authorization" , bearerToken.getBytes(StandardCharsets.UTF_8));
            return msg;
        } );
    }
}
