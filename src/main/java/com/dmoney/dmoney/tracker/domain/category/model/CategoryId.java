package com.dmoney.dmoney.tracker.domain.category.model;

import com.dmoney.dmoney.shared.domain.exceptions.ValidationException;

import java.util.UUID;

public record CategoryId(UUID value) {

    public CategoryId {
        if(value == null){
            throw new ValidationException("Category id cannot be null");
        }
    }

    public static CategoryId from(String value){
        if(value == null || value.isBlank()){
            throw new ValidationException("Invalid category id: " + value);
        }
        try {
            return new CategoryId(UUID.fromString(value));
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Invalid category id: " + value);
        }
    }


    public static CategoryId newId() {
        return new CategoryId(UUID.randomUUID());
    }
}
