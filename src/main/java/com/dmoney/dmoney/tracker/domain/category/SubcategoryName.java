package com.dmoney.dmoney.tracker.domain.category;

public record SubcategoryName(String value) {
    public SubcategoryName {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Subcategory name cannot be blank");
        }
        value = value.trim().toLowerCase();
    }

    public static SubcategoryName from(String value){
        return new SubcategoryName(value);
    }
}
