package com.tthton.audio_converter.uploader.business.impl;

import com.tthton.audio_converter.uploader.exception.AudioFileException;
import com.tthton.audio_converter.uploader.model.dto.FileNameDto;
import com.tthton.audio_converter.uploader.repository.AudioFileRepository;
import com.tthton.audio_converter.uploader.business.AudioFileBusiness;
import com.tthton.audio_converter.uploader.rest.AudioFileClient;
import com.tthton.audio_converter.uploader.util.FileUtil;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Implementation of {@link AudioFileBusiness}
 */
@Slf4j
@Service
public class AudioFileBusinessImpl implements AudioFileBusiness {
    private static final String FILE_SIZE_GAUGE_NAME = "AUDIO_FILE_SIZE";

    private final AudioFileRepository audioFileRepository;

    private final AudioFileClient audioFileClient;

    private final AtomicLong audioFileSizeGauge;

    public AudioFileBusinessImpl(AudioFileRepository audioFileRepository, AudioFileClient audioFileClient, MeterRegistry meterRegistry) {
        this.audioFileRepository = audioFileRepository;
        this.audioFileClient = audioFileClient;

        audioFileSizeGauge = new AtomicLong(0);
        meterRegistry.gauge(FILE_SIZE_GAUGE_NAME, audioFileSizeGauge);
    }

    @Override
    public String saveFile(int userId, String documentType, MultipartFile multipartFile) throws AudioFileException {
        String completeFileName = FileUtil.generateFileName(multipartFile.getOriginalFilename());

        audioFileSizeGauge.set(multipartFile.getSize());

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

        audioFileSizeGauge.set(multipartFile.getSize());

        log.info("Saving file for conversion filename {}", completeFileName);

        String filePath;
        try {
            filePath = audioFileRepository.saveFile(completeFileName, multipartFile);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw AudioFileException.format("Cannot save file : ", e.getMessage());
        }

        FileNameDto fileNameDto = audioFileClient.sendFilePath(completeFileName);

        return fileNameDto.getFilePath();
    }
}
