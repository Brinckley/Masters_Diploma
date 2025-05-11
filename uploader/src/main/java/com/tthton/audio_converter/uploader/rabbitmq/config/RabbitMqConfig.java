package com.tthton.audio_converter.uploader.rabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Configuration class for setting up connection to rabbitMq
 */
@Configuration
public class RabbitMqConfig {
    @Value("${rabbitmq.uploadedQueue}")
    private String uploadedQueue;

    @Value("${rabbitmq.convertedQueue}")
    private String convertedQueue;

    @Bean
    public DirectExchange audioExchange() {
        return new DirectExchange("audio.exchange");
    }

    @Bean
    public Queue audioUploadedQueue() {
        return new Queue(uploadedQueue, true);
    }

    @Bean
    public Queue audioConvertedQueue() {
        return new Queue(convertedQueue, true);
    }

    @Bean
    public Binding uploadedBinding() {
        return BindingBuilder.bind(audioUploadedQueue()).to(audioExchange()).with("uploaded");
    }

    @Bean
    public Binding convertedBinding() {
        return BindingBuilder.bind(audioConvertedQueue()).to(audioExchange()).with("converted");
    }
}