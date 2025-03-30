package com.tthton.audio_converter.uploader.model;

import java.util.Optional;

/**
 * Instrument type for demucs clearing
 */
public enum InstrumentType {
    /**
     * the original track
     */
    ORIGINAL,

    /**
     * the separated vocal track
     */
    VOCALS,

    /**
     * the separated drum track.
     */
    DRUMS,

    /**
     * the separated bass track.
     */
    BASS,

    /**
     * the remaining musical accompaniment
     */
    OTHER;

    int type;

    /**
     * Convert integer value to {@link InstrumentType} type
     *
     * @param type integer value
     * @return optional of instrument type
     */
    public static Optional<InstrumentType> from(int type) {
        for (InstrumentType instrumentType : InstrumentType.values()) {
            if (instrumentType.type == type) {
                return Optional.of(instrumentType);
            }
        }
        return Optional.empty();
    }
}
