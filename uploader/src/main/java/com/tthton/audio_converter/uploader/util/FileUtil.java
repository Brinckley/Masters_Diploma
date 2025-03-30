package com.tthton.audio_converter.uploader.util;

import lombok.experimental.UtilityClass;

import java.util.UUID;

/**
 * Utility class for operations related to files
 */
@UtilityClass
public class FileUtil {
    /**
     * Generating unique name based on the original file name
     *
     * @param originalFileName original file name
     * @return unique file name
     */
    public String generateFileName(String originalFileName) {
        return UUID.randomUUID() + "_" + originalFileName;
    }

    /**
     * Replace the old extension with the new one
     *
     * @param initialFileName initial file name
     * @param newExtension new extension
     * @return edited file name
     */
    public String changeFileExtension(String initialFileName, String newExtension) {
        int lastDot = initialFileName.lastIndexOf(".");
        if (lastDot == -1) {
            return initialFileName + "." + newExtension;
        }
        return initialFileName.substring(0, lastDot) + "." + newExtension;
    }
}
