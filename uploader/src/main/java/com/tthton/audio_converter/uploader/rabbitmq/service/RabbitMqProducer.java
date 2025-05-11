package com.tthton.audio_converter.uploader.rabbitmq.service;

import com.tthton.audio_converter.uploader.model.dto.ConversionAudioDto;
import com.tthton.audio_converter.uploader.rabbitmq.config.RabbitMqConfig;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RabbitMqProducer {
    private static final int REPLY_TIME_OUT_MS = 600_000; // 10 minutes

    private final RabbitTemplate rabbitTemplate;

    @PostConstruct
    private void init() {
        rabbitTemplate.setReplyTimeout(REPLY_TIME_OUT_MS);
    }

    public String sendAudioProcessingRequest(ConversionAudioDto conversionAudioDto) {
        // Send the ConversionAudioDto object as a message
        return (String) rabbitTemplate.convertSendAndReceive(
                "audio.exchange", "uploaded", conversionAudioDto);
    }
}
