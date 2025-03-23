package com.tthton.audio_converter.uploader.repository;

import io.micrometer.core.annotation.Timed;
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
     * @return path to the saved file
     * @throws IOException cannot save the file
     */
    @Timed("savingAudioFile")
    String saveFile(String fileName, MultipartFile multipartFile) throws IOException;

    /**
     * Getting optional of file by its name
     * @param fileName file name
     * @return optional of file
     */
    @Timed("loadingFile")
    Optional<File> loadFile(String fileName);
}
