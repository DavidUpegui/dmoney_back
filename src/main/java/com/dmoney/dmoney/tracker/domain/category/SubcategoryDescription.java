package com.dmoney.dmoney.tracker.domain.category;

import com.dmoney.dmoney.shared.domain.exceptions.ValidationException;

public record SubcategoryDescription(String value) {
    private static final int MAX_LENGTH = 255;

    public SubcategoryDescription(String value) {
        String normalized = value != null ? value.trim() : "";
        if (normalized.length() > MAX_LENGTH) {
            throw new ValidationException("Subcategory description is too long");
        }
        this.value = normalized;
    }

    public static SubcategoryDescription from(String value){
        return new SubcategoryDescription(value);
    }

    public static SubcategoryDescription fromNullable(String value){
        return value == null ? empty() : from(value);
    }

    public static SubcategoryDescription empty(){
        return new SubcategoryDescription("");
    }
}
