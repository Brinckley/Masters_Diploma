package com.tthton.audio_converter.uploader.service.impl;

import com.tthton.audio_converter.uploader.repository.AudioFileRepository;
import com.tthton.audio_converter.uploader.service.AudioFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class AudioFileServiceImpl implements AudioFileService {
    private final AudioFileRepository audioFileRepository;

    @Override
    public String saveFile(int userId, String documentType, MultipartFile multipartFile) {

    }

    @Override
    public Resource loadFileAsResource(String fileName) {
        return null;
    }
}
