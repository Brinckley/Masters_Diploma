package com.tthton.audio_converter.uploader.business;

import com.tthton.audio_converter.uploader.exception.AudioFileException;
import com.tthton.audio_converter.uploader.model.AudioFile;
import com.tthton.audio_converter.uploader.model.dto.AudioRequestDto;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

/**
 * Business logic for dealing with audio file
 */
public interface AudioFileBusiness {
    /**
     * Convert audio file to midi
     *
     * @param audioFile audio file with related data
     * @return midi file as resource
     */
    ByteArrayResource convertFile(AudioFile audioFile) throws AudioFileException;
}
