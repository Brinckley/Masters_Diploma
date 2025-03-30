package com.tthton.audio_converter.uploader.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tthton.audio_converter.uploader.model.InstrumentType;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import org.springframework.web.multipart.MultipartFile;

/**
 * Request object for audioFile for conversion
 */
@Data
@Builder
public class AudioRequestDto {
    private String userId;

    private MultipartFile audioFile;

    private String instrumentType;
}
