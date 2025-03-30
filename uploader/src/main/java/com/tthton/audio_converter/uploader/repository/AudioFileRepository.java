package com.tthton.audio_converter.uploader.repository;

import io.micrometer.observation.annotation.Observed;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

/**
 * Repository for saving audio files and loading midi results
 */
public interface AudioFileRepository {
    /**
     * Save audio file received from user
     *
     * @param fileName file name
     * @param multipartFile file
     * @throws IOException cannot save the file
     */
    @Observed(name = "savingAudioFile")
    void saveAudioFile(String fileName, MultipartFile multipartFile) throws IOException;

    /**
     * Getting optional of midi file by its name
     * @param fileName file name
     * @return optional of file
     */
    @Observed(name = "loadingMidiFile")
    Optional<File> loadMidiFile(String fileName);
}
