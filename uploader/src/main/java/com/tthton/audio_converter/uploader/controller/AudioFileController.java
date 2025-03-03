package com.tthton.audio_converter.uploader.controller;

import com.tthton.audio_converter.uploader.model.dto.FileRequestDto;
import com.tthton.audio_converter.uploader.model.dto.FileResponseDto;
import com.tthton.audio_converter.uploader.service.AudioFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * Rest controller related to downloading and uploading input files
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class AudioFileController {
    private final AudioFileService audioFileService;

    @PostMapping(value = "/uploadAudio", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadAudioFile(@RequestParam("userId") Integer userId,
                                  @RequestParam("documentType") String documentType,
                                  @RequestParam("file") MultipartFile file) {
        FileRequestDto audioFileRequestDto = FileRequestDto.builder().userId(userId)
                .documentType(documentType)
                .multipartFile(file).build();
        log.info("The file with name {} received at uploadFile endpoint",
                audioFileRequestDto.getMultipartFile().getName());

        return audioFileService.saveFile(audioFileRequestDto.getUserId(),
                audioFileRequestDto.getDocumentType(),
                audioFileRequestDto.getMultipartFile());
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
