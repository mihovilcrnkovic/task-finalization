package com.example.task_finalization.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Value("${rabbitmq.finalization.queue.name}")
    String finalizationQueueName;

    @Value("${rabbitmq.finalization.exchange.name}")
    String finalizationExchangeName;

    @Bean
    public Queue finalizationQueue() {
        return new Queue(finalizationQueueName);
    }

    @Bean
    public FanoutExchange finalizationExchange() {
        return new FanoutExchange(finalizationExchangeName);
    }

    @Bean
    public Binding binding(Queue finalizationQueue, FanoutExchange finalizationExchange) {
        return BindingBuilder.bind(finalizationQueue)
                .to(finalizationExchange);
    }

    @Bean
    public JacksonJsonMessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
