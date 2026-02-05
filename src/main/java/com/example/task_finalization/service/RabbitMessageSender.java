package com.example.task_finalization.service;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class RabbitMessageSender {

    final RabbitTemplate rabbitTemplate;
    final FanoutExchange processingJobExchange;
    final AuthenticationService authenticationService;

    public void sendMessage(Object message) {
        String token = authenticationService.getTokenFromContext();
        rabbitTemplate.convertAndSend(processingJobExchange.getName(), "", message, (msg) -> {
            msg.getMessageProperties().setHeader("Authorization " , "Bearer " + token);
            return msg;
        } );
    }
}
