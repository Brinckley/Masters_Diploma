package com.tthton.audio_converter.uploader.repository.impl;

import com.tthton.audio_converter.uploader.exception.AudioFileException;
import com.tthton.audio_converter.uploader.repository.AudioFileRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * In memory realization of {@link AudioFileRepository}
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class AudioFileRepositoryImpl implements AudioFileRepository {
    private static final String SHARED_AUDIO_FOLDER_ENV = "SHARED_AUDIO_FOLDER";

    private static final String SHARED_MIDI_FOLDER_ENV = "SHARED_MIDI_FOLDER";

    private static final String UPLOADS_DIRECTORY_NAME = System.getenv(SHARED_AUDIO_FOLDER_ENV);

    private static final String MIDI_DIRECTORY_NAME = System.getenv(SHARED_MIDI_FOLDER_ENV);

    @PostConstruct
    private void initDirectories() throws AudioFileException {
        try {
            Files.createDirectories(Paths.get(UPLOADS_DIRECTORY_NAME));
            Files.createDirectories(Paths.get(MIDI_DIRECTORY_NAME));
        } catch (IOException e) {
            throw AudioFileException.format("Cannot create uploads and midis folders : %s", e.getMessage());
        }
    }

    @Override
    public void saveAudioFile(String fileName, MultipartFile multipartFile) throws IOException {
        Path filePath = Paths.get(UPLOADS_DIRECTORY_NAME, fileName);

        try (InputStream inputStream = multipartFile.getInputStream()) {
            log.info("Inside save file method with filePath : {}", filePath);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    @Override
    public ByteArrayResource loadMidiFile(String fileName) throws IOException {
        Path filePath = Paths.get(MIDI_DIRECTORY_NAME).resolve(fileName).normalize();

        byte[] midiBytes = Files.readAllBytes(filePath);

        return new ByteArrayResource(midiBytes);
    }
}
