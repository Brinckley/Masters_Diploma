package com.tthton.audio_converter.uploader.business;

import com.tthton.audio_converter.uploader.exception.AudioFileException;
import com.tthton.audio_converter.uploader.model.AudioFile;
import org.springframework.core.io.ByteArrayResource;

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
