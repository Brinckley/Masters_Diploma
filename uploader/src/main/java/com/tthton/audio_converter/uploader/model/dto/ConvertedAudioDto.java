package com.tthton.audio_converter.uploader.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class ConvertedAudioDto {
    @JsonProperty("fileName")
    private String fileName;
}
