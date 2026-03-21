package com.dmoney.dmoney.tracker.domain.category;

import com.dmoney.dmoney.shared.domain.exceptions.ValidationException;

public record SubcategoryName(String value) {
    public SubcategoryName {
        if (value == null || value.isBlank()) {
            throw new ValidationException("Subcategory name cannot be blank");
        }
        value = value.trim();
    }

    public static SubcategoryName from(String value){
        return new SubcategoryName(value);
    }
}
