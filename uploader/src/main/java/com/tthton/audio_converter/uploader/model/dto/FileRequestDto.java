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
public class FileRequestDto {
    private Integer userId;

    private String documentType;

    private MultipartFile file;
}
