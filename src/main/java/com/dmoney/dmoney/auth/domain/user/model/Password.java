package com.dmoney.dmoney.auth.domain.user.model;

import com.dmoney.dmoney.shared.domain.exceptions.ValidationException;

public record Password(String value) {
    private static final int MIN_LENGTH = 8;

    public Password {
        if (value == null || value.isBlank()) {
            throw new ValidationException("Password cannot be null or blank");
        }
        if (value.length() < MIN_LENGTH) {
            throw new ValidationException("Password must be at least 8 characters");
        }
    }

    public static Password from(String value) {
        return new Password(value);
    }
}
