package com.tthton.audio_converter.uploader.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

/**
 * Entity for saving file data
 */
@Data
@Builder
public class AudioFile {
    @JsonProperty("documentId")
    private Integer documentId;

    @JsonProperty("userId")
    private Integer userId;

    @JsonProperty("fileName")
    private String fileName;

    @JsonProperty("documentType")
    private String documentType;

    @JsonProperty("documentFormat")
    private String documentFormat;

    @JsonProperty("uploadDirectory")
    private String uploadDirectory;
}
