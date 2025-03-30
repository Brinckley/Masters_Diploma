package com.tthton.audio_converter.uploader.business.impl;

import com.tthton.audio_converter.uploader.exception.AudioFileException;
import com.tthton.audio_converter.uploader.model.dto.AudioRequestDto;
import com.tthton.audio_converter.uploader.model.dto.ConversionAudioDto;
import com.tthton.audio_converter.uploader.repository.AudioFileRepository;
import com.tthton.audio_converter.uploader.business.AudioFileBusiness;
import com.tthton.audio_converter.uploader.rest.AudioFileClient;
import com.tthton.audio_converter.uploader.util.FileUtil;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
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
    public Resource convertFile(AudioRequestDto audioRequestDto) throws AudioFileException {
        MultipartFile audioFile = audioRequestDto.getAudioFile();
        audioFileSizeGauge.set(audioFile.getSize());

        String completeFileName = FileUtil.generateFileName(audioFile.getOriginalFilename());
        saveFile(completeFileName, audioFile);

        ConversionAudioDto conversionAudioDto = ConversionAudioDto.builder()
                .fileName(completeFileName)
                .instrumentType(audioRequestDto.getInstrumentType().getType())
                .build();

        String midiFileName = audioFileClient.sendPostRequest(conversionAudioDto);

        File file = audioFileRepository.loadMidiFile(midiFileName)
                .orElseThrow(() -> AudioFileException.format("Cannot load midi result for name %s", midiFileName));

        return new FileSystemResource(file);
    }

    private void saveFile(String completeFileName, MultipartFile audioFile) throws AudioFileException {
        try {
            audioFileRepository.saveAudioFile(completeFileName, audioFile);
        } catch (IOException e) {
            log.error("Cannot save the audioFile in repository {}", e.getMessage());
            throw AudioFileException.format("Cannot save file : ", e.getMessage());
        }
    }
}
