package com.example.task_finalization.configuration;

import com.example.task_finalization.model.ProcessingJob;
import com.example.task_finalization.service.AuthorizationInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;

@Configuration
@RequiredArgsConstructor
public class KafkaConfig {

    private final AuthorizationInterceptor authorizationInterceptor;
    private final ConsumerFactory<String, ProcessingJob> consumerFactory;

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ProcessingJob> kafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, ProcessingJob> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory);
        factory.setRecordInterceptor(authorizationInterceptor);

        return factory;
    }

}
