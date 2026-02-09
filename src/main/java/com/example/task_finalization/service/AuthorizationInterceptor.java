package com.example.task_finalization.service;

import com.example.task_finalization.model.ProcessingJob;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.jspecify.annotations.Nullable;
import org.springframework.kafka.listener.RecordInterceptor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class AuthorizationInterceptor implements RecordInterceptor<String, ProcessingJob> {

    private final AuthenticationService authenticationService;

    @Override
    public @Nullable ConsumerRecord<String, ProcessingJob> intercept(ConsumerRecord<String, ProcessingJob> record, Consumer<String, ProcessingJob> consumer) {
        Header authHeader = record.headers().lastHeader("Authorization");

        if (authHeader != null) {
            String authorization = new String(authHeader.value(), StandardCharsets.UTF_8);
            authenticationService.setContextAuthentication(authorization);
        }

        return record;
    }

    @Override
    public void afterRecord(ConsumerRecord<String, ProcessingJob> record, Consumer<String, ProcessingJob> consumer) {
        SecurityContextHolder.clearContext();
    }
}
