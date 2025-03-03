package com.tthton.audio_converter.uploader.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import org.springframework.web.multipart.MultipartFile;

/**
 * Entity for parsing file upload request
 */
@Data
@Builder
@Jacksonized
public class FileRequestDto {
    @JsonProperty("userId")
    private Integer userId;

    @JsonProperty("documentType")
    private String documentType;

    @JsonProperty("file")
    private MultipartFile multipartFile;
}
