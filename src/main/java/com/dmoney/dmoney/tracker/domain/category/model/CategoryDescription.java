package com.dmoney.dmoney.tracker.domain.category.model;

import com.dmoney.dmoney.shared.domain.exceptions.ValidationException;

public record CategoryDescription(String value) {

    private static final int MAX_LENGTH = 255;

    public CategoryDescription(String value) {
        String normalized = value != null ? value.trim() : "";
        if (normalized.length() > MAX_LENGTH) {
            throw new ValidationException("Category description is too long");
        }
        this.value = normalized;
    }

    public static CategoryDescription from(String value){
        return new CategoryDescription(value);
    }

    public static CategoryDescription fromNullable(String value){
        return value == null ? empty() : from(value);
    }

    public static CategoryDescription empty(){
        return new CategoryDescription("");
    }
}
