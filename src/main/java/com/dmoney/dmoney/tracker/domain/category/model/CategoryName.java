package com.dmoney.dmoney.tracker.domain.category.model;

import com.dmoney.dmoney.shared.domain.exceptions.ValidationException;

public record CategoryName(String value) {

    public CategoryName {
        if (value == null || value.isBlank()) {
            throw new ValidationException("Category name cannot be empty");
        }
        value = value.trim();
    }

    public static CategoryName from(String value){
        return new CategoryName(value);
    }
}
