package com.dmoney.dmoney.tracker.domain.category;

import java.util.Objects;
import java.util.UUID;

public record SubcategoryId(UUID value) {

    public SubcategoryId {
        Objects.requireNonNull(value, "Subcategory ID cannot be null");
    }

    public static SubcategoryId from(String value){
        return new SubcategoryId(UUID.fromString(value));
    }

    public static SubcategoryId newId() {
        return new SubcategoryId(UUID.randomUUID());
    }
}

