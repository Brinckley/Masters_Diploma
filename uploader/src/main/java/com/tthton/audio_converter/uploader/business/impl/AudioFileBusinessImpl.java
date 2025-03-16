package com.tthton.audio_converter.uploader.business.impl;

import com.tthton.audio_converter.uploader.exception.AudioFileException;
import com.tthton.audio_converter.uploader.repository.AudioFileRepository;
import com.tthton.audio_converter.uploader.business.AudioFileBusiness;
import com.tthton.audio_converter.uploader.rest.AudioFileClient;
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

/**
 * Implementation of {@link AudioFileBusiness}
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AudioFileBusinessImpl implements AudioFileBusiness {
    private final AudioFileRepository audioFileRepository;

    private final AudioFileClient audioFileClient;

    @Override
    public String saveFile(int userId, String documentType, MultipartFile multipartFile) throws AudioFileException {
        String completeFileName = FileUtil.generateFileName(multipartFile.getOriginalFilename());

        log.info("New filename {}", completeFileName);

        try {
            audioFileRepository.saveFile(completeFileName, multipartFile);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw AudioFileException.format("Cannot save file : ", e.getMessage());
        }
        return completeFileName;
    }

    @Override
    public Resource loadFileAsResource(String fileName) {
        File file = audioFileRepository.loadFile(fileName)
                .orElseThrow(() -> AudioFileException.format("Cannot get file with filename %s", fileName));

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

    @Override
    public String sendFileToBasicPitch(MultipartFile multipartFile) throws AudioFileException {
        String completeFileName = FileUtil.generateFileName(multipartFile.getOriginalFilename());

        log.info("Saving file for conversion filename {}", completeFileName);

        String filePath;
        try {
            filePath = audioFileRepository.saveFile(completeFileName, multipartFile);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw AudioFileException.format("Cannot save file : ", e.getMessage());
        }

        audioFileClient.sendFilePath(completeFileName);

        return completeFileName;
    }
}
