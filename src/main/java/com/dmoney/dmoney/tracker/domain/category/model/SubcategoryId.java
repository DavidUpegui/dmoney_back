package com.dmoney.dmoney.tracker.domain.category.model;

import com.dmoney.dmoney.shared.domain.exceptions.ValidationException;

import java.util.UUID;

public record SubcategoryId(UUID value) {

    public SubcategoryId {
        if(value == null){
            throw new ValidationException("Subcategory id cannot be null");
        }
    }

    public static SubcategoryId from(String value){
        if(value == null || value.isBlank()){
            throw new ValidationException("Invalid subcategory id: " + value);
        }
        try {
            return new SubcategoryId(UUID.fromString(value));
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Invalid subcategory id: " + value);
        }
    }

    public static SubcategoryId newId() {
        return new SubcategoryId(UUID.randomUUID());
    }
}

