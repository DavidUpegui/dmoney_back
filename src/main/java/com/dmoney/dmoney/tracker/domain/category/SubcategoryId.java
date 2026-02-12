package com.dmoney.dmoney.tracker.domain.category;

import java.util.Objects;
import java.util.UUID;

public record SubcategoryId(UUID value) {

    public SubcategoryId(UUID value) {
        this.value = Objects.requireNonNull(value);
    }

    public static SubcategoryId newId() {
        return new SubcategoryId(UUID.randomUUID());
    }
}

