package com.tthton.audio_converter.uploader.repository;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public interface AudioFileRepository {
    void saveFile(Path filePath, MultipartFile multipartFile) throws IOException;

    File loadFile(Path filePath);
}
