package com.tthton.audio_converter.uploader.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

/**
 * Instrument type for demucs clearing
 */
@Getter
@AllArgsConstructor
public enum InstrumentType {
    /**
     * Original track
     */
    ORIGINAL("original"),

    /**
     * Separated vocal track
     */
    VOCALS("vocals"),

    /**
     * Separated drum track.
     */
    DRUMS("drums"),

    /**
     * Separated bass track.
     */
    BASS("bass"),

    /**
     * Remaining musical accompaniment
     */
    OTHER("other");

    /**
     * Type name
     */
    final String type;

    /**
     * Convert string value to {@link InstrumentType} type
     *
     * @param type string value
     * @return optional of instrument type
     */
    public static Optional<InstrumentType> from(String type) {
        if (type == null) {
            return Optional.empty();
        }

        for (InstrumentType instrumentType : InstrumentType.values()) {
            if (instrumentType.type.equals(type.toLowerCase())) {
                return Optional.of(instrumentType);
            }
        }
        return Optional.empty();
    }
}
