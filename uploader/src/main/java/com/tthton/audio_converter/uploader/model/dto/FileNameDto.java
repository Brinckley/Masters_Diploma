package com.tthton.audio_converter.uploader.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

/**
 * Entity to send data to neural network client
 */
@Data
@Builder
@Jacksonized
public class FileNameDto {
    @JsonProperty("filePath")
    private String filePath;
}
