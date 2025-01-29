package com.tthton.audio_converter.uploader.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface AudioFileService {
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
}
