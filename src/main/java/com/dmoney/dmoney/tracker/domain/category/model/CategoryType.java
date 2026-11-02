package com.dmoney.dmoney.tracker.domain.category.model;

import com.dmoney.dmoney.shared.domain.exceptions.ValidationException;

public enum CategoryType {
    INCOME,
    OUTCOME;

    public static CategoryType from(String value) {
        if (value == null || value.isBlank()) {
            throw new ValidationException("CategoryType cannot be null");
        }
        try {
            return CategoryType.valueOf(value);
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Invalid CategoryType: " + value);
        }
    }
}
