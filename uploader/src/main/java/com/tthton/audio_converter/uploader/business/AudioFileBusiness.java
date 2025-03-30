package com.tthton.audio_converter.uploader.business;

import com.tthton.audio_converter.uploader.exception.AudioFileException;
import com.tthton.audio_converter.uploader.model.dto.AudioRequestDto;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

/**
 * Business logic for dealing with audio file
 */
public interface AudioFileBusiness {
    /**
     * Convert audio file to midi
     *
     * @param audioRequestDto audio file with related data
     * @return midi file as resource
     */
    Resource convertFile(AudioRequestDto audioRequestDto) throws AudioFileException;
}
