package com.tthton.audio_converter.uploader.rest;

import com.tthton.audio_converter.uploader.exception.AudioFileException;
import com.tthton.audio_converter.uploader.model.dto.ConversionAudioDto;
import com.tthton.audio_converter.uploader.model.dto.FileNameDto;
import io.micrometer.core.annotation.Timed;

/**
 * Functions for communication with app that uses the convertor NN
 */
public interface AudioFileClient {
    /**
     * Ping healthcheck to the neural network worker service
     *
     * @param message message
     * @return response message
     * @throws AudioFileException cannot handle request
     */
    String pingEcho(String message) throws AudioFileException;

    /**
     * Send data to the next microservice
     *
     * @param conversionAudioDto data for conversion
     * @return name of the converted file
     */
    String sendGetRequest(ConversionAudioDto conversionAudioDto) throws AudioFileException;
}
