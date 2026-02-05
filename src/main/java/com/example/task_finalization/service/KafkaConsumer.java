package com.example.task_finalization.service;

import com.example.task_finalization.model.ProcessingJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConsumer {

    final JsonMapper jsonMapper;
    final FinalizationService finalizationService;
    final AuthenticationService authenticationService;

    @KafkaListener(topics = "my-topic")
    public void consumeMessage(ProcessingJob message, @Header("Authorization") String authorization){
        String stringMsg = jsonMapper.writeValueAsString(message);
        try {
            authenticationService.setContextAuthentication(authorization);
            log.info("CONSUMED MESSAGE: " + stringMsg);

            finalizationService.finalizeJob(message);
        } catch (Exception e) {
            String errorMsg = "Exception trying to receive message: " +
                    stringMsg + "\n\n" +
                    e.getMessage();
            log.error(errorMsg);
        } finally {
            SecurityContextHolder.clearContext();
        }

    }
}
