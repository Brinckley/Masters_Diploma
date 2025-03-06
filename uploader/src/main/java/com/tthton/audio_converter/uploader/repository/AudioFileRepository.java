package com.tthton.audio_converter.uploader.repository;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

public interface AudioFileRepository {
    /**
     * Save audio file received from user
     *
     * @param fileName file name
     * @param multipartFile file
     * @throws IOException cannot save the file
     */
    void saveFile(String fileName, MultipartFile multipartFile) throws IOException;

    /**
     * Getting optional of file by its name
     * @param fileName file name
     * @return optional of file
     */
    Optional<File> loadFile(String fileName);
}
