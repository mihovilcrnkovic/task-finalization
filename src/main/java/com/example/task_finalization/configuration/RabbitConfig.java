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
    public Queue finalizationQueue() {
        return new Queue("finalization-queue");
    }

    @Bean
    public FanoutExchange finalizationExchange() {
        return new FanoutExchange("finalization-exchange");
    }

    @Bean
    public Binding binding(Queue finalizationQueue, FanoutExchange finalizationExchange) {
        return BindingBuilder.bind(finalizationQueue)
                .to(finalizationExchange);
    }
}
