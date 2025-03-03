package com.tthton.audio_converter.uploader.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Entity for sending response after uploading file
 */
@Data
@Builder
@AllArgsConstructor
public class FileResponseDto {
    @JsonProperty("fileName")
    private String fileName;

    @JsonProperty("fileDownloadUri")
    private String fileDownloadUri;

    @JsonProperty("fileType")
    private String fileType;

    @JsonProperty("fileSize")
    private Long fileSize;
}
