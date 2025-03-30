package com.tthton.audio_converter.uploader.repository;

import io.micrometer.observation.annotation.Observed;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
     *
     * @param fileName file name
     * @return optional of file
     */
    @Observed(name = "loadingMidiFile")
    ByteArrayResource loadMidiFile(String fileName) throws IOException;
}
