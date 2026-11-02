package com.dmoney.dmoney.tracker.domain.category.model;

import com.dmoney.dmoney.shared.domain.exceptions.ValidationException;

/**
 * Opaque icon identifier supplied by the client. The domain stores it as text
 * and never interprets it.
 */
public record CategoryIcon(String value) {

    private static final int MAX_LENGTH = 48;

    public CategoryIcon {
        if (value == null || value.isBlank()) {
            throw new ValidationException("Icon cannot be empty");
        }
        value = value.trim();
        if (value.length() > MAX_LENGTH) {
            throw new ValidationException("Icon is too long");
        }
    }

    public static CategoryIcon from(String value) {
        return new CategoryIcon(value);
    }

    /**
     * Returns {@code null} when the value is absent or blank: no icon was
     * chosen, or the client asked to clear it.
     */
    public static CategoryIcon fromNullable(String value) {
        return value == null || value.isBlank() ? null : new CategoryIcon(value);
    }
}
