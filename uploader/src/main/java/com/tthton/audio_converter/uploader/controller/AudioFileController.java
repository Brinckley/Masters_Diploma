package com.tthton.audio_converter.uploader.controller;

import com.tthton.audio_converter.uploader.model.dto.FileRequestDto;
import com.tthton.audio_converter.uploader.business.AudioFileBusiness;
import io.micrometer.core.annotation.Timed;
import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Rest controller related to downloading and uploading input files
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class AudioFileController {
    private final AudioFileBusiness audioFileService;

    @Observed(name = "uploadAudioEndpoint")
    @PostMapping(value = "/uploadAudio", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadAudioFile(@ModelAttribute FileRequestDto audioFileRequestDto) {
        log.info("The file with name {} received at uploadFile endpoint",
                audioFileRequestDto);

        return audioFileService.saveFile(audioFileRequestDto.getUserId(),
                audioFileRequestDto.getDocumentType(),
                audioFileRequestDto.getFile());
    }

    @Observed(name = "testUploadAudioEndpoint")
    @PostMapping(value = "/testUploadAudio", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String testUploadAudioFile(@ModelAttribute FileRequestDto audioFileRequestDto) {
        log.info("TEST : The file with name {} received at testUploadFile endpoint",
                audioFileRequestDto);

        return audioFileService.sendFileToBasicPitch(audioFileRequestDto.getFile());
    }

    @GetMapping("/downloadAudio/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
        Resource audioResource = audioFileService.loadFileAsResource(fileName);

        if (!audioResource.exists()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(audioResource);
    }
}
