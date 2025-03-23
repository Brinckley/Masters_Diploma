package com.tthton.audio_converter.uploader.rest;

import com.tthton.audio_converter.uploader.exception.AudioFileException;
import com.tthton.audio_converter.uploader.model.dto.FileNameDto;
import io.micrometer.core.annotation.Timed;

/**
 * Functions for communication with app that uses the convertor NN
 */
public interface AudioFileClient {
    /**
     * Sending file path to the neural network worker
     *
     * @param pathToSavedFile path to saved file
     * @throws AudioFileException cannot handle request
     */
    @Timed("sendingAudioFile")
    FileNameDto sendFilePath(String pathToSavedFile) throws AudioFileException;

    /**
     * Ping healthcheck to the neural network worker service
     *
     * @param message message
     * @return response message
     * @throws AudioFileException cannot handle request
     */
    @Timed("pingEcho")
    String pingEcho(String message) throws AudioFileException;
}
