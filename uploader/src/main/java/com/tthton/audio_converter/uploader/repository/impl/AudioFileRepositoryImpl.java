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

@Slf4j
@Repository
@RequiredArgsConstructor
public class AudioFileRepositoryImpl implements AudioFileRepository {
    @Override
    public void saveFile(Path filePath, MultipartFile multipartFile) throws IOException {
        Files.createDirectories(Paths.get("uploads/"));

        try (InputStream inputStream = multipartFile.getInputStream()) {
            log.info("Inside save file method with filePath : {}", filePath);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    @Override
    public File loadFile(Path filePath) {
        return new File(filePath.toString());
    }
}
