package com.dmoney.dmoney.tracker.domain.category;

import com.dmoney.dmoney.shared.domain.exceptions.ValidationException;

import java.util.Objects;
import java.util.UUID;

public record CategoryId(UUID value) {

    public CategoryId {
        if(value == null){
            throw new ValidationException("Category id cannot be null");
        }
    }

    public static CategoryId from(String value){
        return new CategoryId(UUID.fromString(value));
    }


    public static CategoryId newId() {
        return new CategoryId(UUID.randomUUID());
    }
}
