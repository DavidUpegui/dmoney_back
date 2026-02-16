package com.dmoney.dmoney.tracker.domain.category;

public record CategoryName(String value) {

    public CategoryName {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Category name cannot be empty");
        }
        value = value.trim();
    }

    public static CategoryName from(String value){
        return new CategoryName(value);
    }
}
