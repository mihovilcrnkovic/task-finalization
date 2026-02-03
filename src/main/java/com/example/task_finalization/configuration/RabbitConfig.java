package com.example.task_finalization.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    public Queue processingJobQueue() {
        return new Queue("processing-job-queue");
    }

    @Bean
    public FanoutExchange processingJobExchange() {
        return new FanoutExchange("processing-job-exchange");
    }

    @Bean
    public Binding binding(Queue processingJobQueue, FanoutExchange processingJobExchange) {
        return BindingBuilder.bind(processingJobQueue)
                .to(processingJobExchange);
    }
}
