package com.tthton.audio_converter.uploader.business;

import com.tthton.audio_converter.uploader.exception.AudioFileException;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

/**
 * Business logic for dealing with audio file
 */
public interface AudioFileBusiness {
    /**
     * Saving file with its basic information
     *
     * @param userId user id
     * @param documentType type of the document
     * @param multipartFile multipart file
     * @return file name
     */
    String saveFile(int userId, String documentType, MultipartFile multipartFile);

    /**
     * Load file by name
     *
     * @param fileName file name
     * @return file as resource
     */
    Resource loadFileAsResource(String fileName);

    /**
     * Convert audio file to midi
     *
     * @param multipartFile audio file
     * @return midi file as resource
     */
    Resource convertFile(MultipartFile multipartFile) throws AudioFileException;

    /**'
     * Sending file to the conversion
     * @param multipartFile audio file
     * @return converted file
     * @throws AudioFileException if unable to handle the file
     */
    String sendFileToBasicPitch(MultipartFile multipartFile) throws AudioFileException;
}
