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
        return new SubcategoryId(UUID.fromString(value));
    }

    public static SubcategoryId newId() {
        return new SubcategoryId(UUID.randomUUID());
    }
}

