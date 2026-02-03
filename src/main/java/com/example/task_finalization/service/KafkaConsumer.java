package com.example.task_finalization.service;

import com.example.task_finalization.model.ProcessingJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConsumer {

    final JsonMapper jsonMapper;

    @KafkaListener(topics = "my-topic")
    public void consumeMessage(ProcessingJob message){
        String stringMsg = jsonMapper.writeValueAsString(message);
        log.info("CONSUMED MESSAGE: " + stringMsg);
    }
}
