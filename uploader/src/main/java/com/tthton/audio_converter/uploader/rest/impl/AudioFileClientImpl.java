package com.tthton.audio_converter.uploader.rest.impl;

import com.tthton.audio_converter.uploader.exception.AudioFileException;
import com.tthton.audio_converter.uploader.model.dto.ConversionAudioDto;
import com.tthton.audio_converter.uploader.model.dto.FileNameDto;
import com.tthton.audio_converter.uploader.rest.AudioFileClient;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Implementation of {@link AudioFileClient}
 */
@Slf4j
@Service
public class AudioFileClientImpl implements AudioFileClient {
    private static final String URL_FORMAT = "http://%s:%s";

    private static final String ENV_NAME_HOST = "NEURAL_WORKER_HOST";

    private static final String ENV_NAME_PORT = "NEURAL_WORKER_PORT";

    private static final String CONVERT_PATH = "/convert";

    private static final String HEALTH_CHECK_PATH = "/healthcheck";

    private final RestTemplate restTemplate = new RestTemplate();

    private final String NEURAL_WORKER_HOST = System.getenv(ENV_NAME_HOST);

    private final String NEURAL_WORKER_PORT = System.getenv(ENV_NAME_PORT);

    private final String NEURAL_WORKER_URL = String.format(URL_FORMAT, NEURAL_WORKER_HOST, NEURAL_WORKER_PORT);

    @Override
    public String pingEcho(String message) throws AudioFileException {
        String url = NEURAL_WORKER_URL + HEALTH_CHECK_PATH;

        log.info("Sending healthcheck ping to url : {}", url);

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw AudioFileException.format("Failed to get ping response from converter client. Status code %d",
                    response.getStatusCode().value());
        }

        return response.getBody();
    }

    @Override
    public String sendPostRequest(ConversionAudioDto conversionAudioDto) throws AudioFileException {
        String url = NEURAL_WORKER_URL + CONVERT_PATH;
        log.info("Sending file path to url : {}", url);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ConversionAudioDto> request = new HttpEntity<>(conversionAudioDto, httpHeaders);

        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw AudioFileException.format("Failed to get ping response from converter client. Status code %d",
                    response.getStatusCode().value());
        }

        return response.getBody();
    }
}
