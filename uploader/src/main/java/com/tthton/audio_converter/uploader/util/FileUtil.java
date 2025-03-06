package com.tthton.audio_converter.uploader.util;

import lombok.experimental.UtilityClass;

import java.util.UUID;

/**
 * Utility class for operations related to files
 */
@UtilityClass
public class FileUtil {
    public String generateFileName(String originalFileName) {
        return UUID.randomUUID() + "_" + originalFileName;
    }
}
