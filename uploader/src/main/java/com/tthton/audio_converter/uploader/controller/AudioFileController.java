package com.tthton.audio_converter.uploader.controller;

import com.tthton.audio_converter.uploader.model.dto.FileRequestDto;
import com.tthton.audio_converter.uploader.model.dto.FileResponseDto;
import com.tthton.audio_converter.uploader.service.AudioFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AudioFileController {
    private static final String DOWNLOAD_FILE_PATH = "/downloadFile/";

    private final AudioFileService audioFileService;

    @PostMapping("/uploadAudio")
    public FileResponseDto uploadAudioFile(@RequestBody FileRequestDto audioFileRequestDto) {
        String fileName = audioFileService.saveFile(audioFileRequestDto.getUserId(),
                audioFileRequestDto.getDocumentType(),
                audioFileRequestDto.getMultipartFile());

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(DOWNLOAD_FILE_PATH)
                .path(fileName)
                .toUriString();

        return FileResponseDto.builder()
                .fileName(fileName)
                .fileDownloadUri(fileDownloadUri)
                .fileType(audioFileRequestDto.getMultipartFile().getContentType())
                .fileSize(audioFileRequestDto.getMultipartFile().getSize())
                .build();
    }
}
