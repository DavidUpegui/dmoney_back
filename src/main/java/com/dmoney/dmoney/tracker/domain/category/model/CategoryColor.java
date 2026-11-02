package com.dmoney.dmoney.tracker.domain.category.model;

import com.dmoney.dmoney.shared.domain.exceptions.ValidationException;

/**
 * Opaque color token supplied by the client. The domain stores it as text and
 * never interprets it, so the palette lives entirely in the client.
 */
public record CategoryColor(String value) {

    private static final int MAX_LENGTH = 32;

    public CategoryColor {
        if (value == null || value.isBlank()) {
            throw new ValidationException("Color cannot be empty");
        }
        value = value.trim();
        if (value.length() > MAX_LENGTH) {
            throw new ValidationException("Color is too long");
        }
    }

    public static CategoryColor from(String value) {
        return new CategoryColor(value);
    }

    /**
     * Returns {@code null} when the value is absent or blank: no color was
     * chosen, or the client asked to clear it.
     */
    public static CategoryColor fromNullable(String value) {
        return value == null || value.isBlank() ? null : new CategoryColor(value);
    }
}
