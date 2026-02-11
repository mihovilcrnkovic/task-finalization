package com.example.task_finalization.kafka;

import com.example.task_finalization.model.ProcessingJob;
import com.example.task_finalization.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.jspecify.annotations.Nullable;
import org.springframework.kafka.listener.RecordInterceptor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthorizationInterceptor implements RecordInterceptor<String, ProcessingJob> {

    private final AuthenticationService authenticationService;
    private final JsonMapper jsonMapper;

    @Override
    public @Nullable ConsumerRecord<String, ProcessingJob> intercept(ConsumerRecord<String, ProcessingJob> record, Consumer<String, ProcessingJob> consumer) {
        Header authHeader = record.headers().lastHeader("Authorization");

        if (authHeader != null) {
            String authorization = new String(authHeader.value(), StandardCharsets.UTF_8);
            authenticationService.setContextAuthentication(authorization);
        } else {
            String valueStr = jsonMapper.writeValueAsString(record.value());
            log.warn("Could not read Authorization for record: " + valueStr);
            return null;
        }

        return record;
    }

    @Override
    public void afterRecord(ConsumerRecord<String, ProcessingJob> record, Consumer<String, ProcessingJob> consumer) {
        SecurityContextHolder.clearContext();
    }
}
