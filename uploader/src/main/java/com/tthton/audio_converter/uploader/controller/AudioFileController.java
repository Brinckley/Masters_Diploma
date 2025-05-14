package com.tthton.audio_converter.uploader.controller;

import com.tthton.audio_converter.uploader.model.AudioFile;
import com.tthton.audio_converter.uploader.model.InstrumentType;
import com.tthton.audio_converter.uploader.model.dto.AudioRequestDto;
import com.tthton.audio_converter.uploader.business.AudioFileBusiness;
import com.tthton.audio_converter.uploader.util.FileUtil;
import io.micrometer.observation.annotation.Observed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Rest controller related to downloading and uploading input files
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
public class AudioFileController {
    private final AudioFileBusiness audioFileService;

    @Observed(name = "convertToMidi")
    @GetMapping(value = "/convert_audio", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Resource> convertToMidi(@Valid @ModelAttribute AudioRequestDto audioRequestDto) {
        AudioFile audioFile = AudioFile.builder()
                .userId(audioRequestDto.getUserId())
                .instrumentType(InstrumentType.from(audioRequestDto.getInstrumentType()).orElse(InstrumentType.OTHER))
                .audioFile(audioRequestDto.getAudioFile())
                .build();

        log.info("The file received for conversion with the name {}", audioRequestDto.getAudioFile().getName());

        Resource resource = audioFileService.convertFile(audioFile);

        String newFileName = FileUtil.changeFileExtension(audioRequestDto.getAudioFile().getOriginalFilename(), ".mid");
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("audio/midi"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + newFileName + "\"")
                .body(resource);
    }
}
