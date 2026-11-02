package com.dmoney.dmoney.tracker.domain.category.model;

import com.dmoney.dmoney.shared.domain.exceptions.ValidationException;

public record SubcategoryName(String value) {
    private static final int MAX_LENGTH = 255;

    public SubcategoryName {
        if (value == null || value.isBlank()) {
            throw new ValidationException("Subcategory name cannot be blank");
        }
        value = value.trim();
        if (value.length() > MAX_LENGTH) {
            throw new ValidationException("Subcategory name is too long");
        }
    }

    public static SubcategoryName from(String value){
        return new SubcategoryName(value);
    }
}
