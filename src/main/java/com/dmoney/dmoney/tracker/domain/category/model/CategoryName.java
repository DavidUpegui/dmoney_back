package com.dmoney.dmoney.tracker.domain.category.model;

import com.dmoney.dmoney.shared.domain.exceptions.ValidationException;

public record CategoryName(String value) {

    private static final int MAX_LENGTH = 255;

    public CategoryName {
        if (value == null || value.isBlank()) {
            throw new ValidationException("Category name cannot be empty");
        }
        value = value.trim();
        if (value.length() > MAX_LENGTH) {
            throw new ValidationException("Category name is too long");
        }
    }

    public static CategoryName from(String value){
        return new CategoryName(value);
    }
}
