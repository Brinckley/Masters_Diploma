package com.tthton.audio_converter.uploader.business.impl;

import com.tthton.audio_converter.uploader.exception.AudioFileException;
import com.tthton.audio_converter.uploader.repository.AudioFileRepository;
import com.tthton.audio_converter.uploader.business.AudioFileBusiness;
import com.tthton.audio_converter.uploader.util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Implementation of {@link AudioFileBusiness}
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AudioFileBusinessImpl implements AudioFileBusiness {
    private static final String FILES_STORAGE_DIRECTORY_NAME = "uploads/";

    private final AudioFileRepository audioFileRepository;

    @Override
    public String saveFile(int userId, String documentType, MultipartFile multipartFile) throws AudioFileException {
        String completeFileName = FileUtil.generateFileName(multipartFile.getOriginalFilename());
        Path filePath = Paths.get(FILES_STORAGE_DIRECTORY_NAME, completeFileName);

        log.info("New filename {}", filePath.getFileName());

        try {
            audioFileRepository.saveFile(filePath, multipartFile);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw AudioFileException.format("Cannot save file : ", e.getMessage());

        }
        return filePath.toString();
    }

    @Override
    public Resource loadFileAsResource(String fileName) {
        Path filePath = Paths.get(FILES_STORAGE_DIRECTORY_NAME).resolve(fileName).normalize();

        File file = audioFileRepository.loadFile(filePath);
        if (!file.exists()) {
            log.error("File does not exist : {}", file.getAbsoluteFile());
        }

        Resource resource;
        try {
            resource = new UrlResource(file.toURI());
        } catch (MalformedURLException e) {
            throw AudioFileException.format("Cannot get file with filename %s", fileName);
        }
        return resource;
    }

    @Override
    public Resource convertFile(MultipartFile multipartFile) throws AudioFileException {
        return null;
    }
}
