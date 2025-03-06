package com.tthton.audio_converter.uploader.rest.impl;

import com.tthton.audio_converter.uploader.exception.AudioFileException;
import com.tthton.audio_converter.uploader.model.dto.FileNameDto;
import com.tthton.audio_converter.uploader.rest.AudioFileClient;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Implementation of {@link AudioFileClient}
 */
@Service
public class AudioFileClientImpl implements AudioFileClient {
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public FileNameDto sendFile(String pathToSavedFile) throws AudioFileException{
        String url = "http://localhost:8081/receive";

        FileNameDto fileNameDto = FileNameDto.builder().filePath(pathToSavedFile).build();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<FileNameDto> request = new HttpEntity<>(fileNameDto, httpHeaders);

        // Send POST request
        ResponseEntity<FileNameDto> response = restTemplate.postForEntity(url, request, FileNameDto.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw AudioFileException.format("Failed to get object from converter client. Status code %d", response.getStatusCode().value());
        }

        return response.getBody();
    }

    @Override
    public String pingEcho(String message) throws AudioFileException {
        String url = "http://basic-pitch-worker:8000/healthcheck";

        // Send POST request
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw AudioFileException.format("Failed to get object from converter client. Status code %d", response.getStatusCode().value());
        }

        return response.getBody();
    }
}
