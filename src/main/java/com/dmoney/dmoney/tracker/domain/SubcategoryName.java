package com.dmoney.dmoney.tracker.domain;

public record SubcategoryName(String value) {
    public SubcategoryName {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Subcategory name cannot be blank");
        }
        value = value.trim().toLowerCase();
    }
}
