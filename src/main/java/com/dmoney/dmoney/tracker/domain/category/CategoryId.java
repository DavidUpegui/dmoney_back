package com.dmoney.dmoney.tracker.domain.category;

import java.util.Objects;
import java.util.UUID;

public record CategoryId(UUID value) {
    public CategoryId {
        Objects.requireNonNull(value, "Category ID cannot be null");
    }

    public static CategoryId from(String value){
        return new CategoryId(UUID.fromString(value));
    }


    public static CategoryId newId() {
        return new CategoryId(UUID.randomUUID());
    }
}
