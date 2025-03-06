package com.tthton.audio_converter.uploader.repository.impl;

import com.tthton.audio_converter.uploader.exception.AudioFileException;
import com.tthton.audio_converter.uploader.repository.AudioFileRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

/**
 * In memory realization of {@link AudioFileRepository}
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class AudioFileRepositoryImpl implements AudioFileRepository {
    private static final String UPLOADS_DIRECTORY_NAME = "uploads/";

    @PostConstruct
    private void initDirectories() throws AudioFileException {
        try {
            Files.createDirectories(Paths.get(UPLOADS_DIRECTORY_NAME));
        } catch (IOException e) {
            throw AudioFileException.format("Cannot create uploads folder : %s", e.getMessage());
        }
    }

    @Override
    public void saveFile(String fileName, MultipartFile multipartFile) throws IOException {
        Path filePath = Paths.get(UPLOADS_DIRECTORY_NAME, fileName);

        try (InputStream inputStream = multipartFile.getInputStream()) {
            log.info("Inside save file method with filePath : {}", filePath);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    @Override
    public Optional<File> loadFile(String fileName) {
        Path filePath = Paths.get(UPLOADS_DIRECTORY_NAME).resolve(fileName).normalize();

        File file = new File(filePath.toString());
        return file.exists() ? Optional.of(file) : Optional.empty();
    }
}
