package com.dmoney.dmoney.tracker.domain.category;

import java.util.Objects;
import java.util.UUID;

public record CategoryId(UUID value) {
    public CategoryId(UUID value) {
        this.value = Objects.requireNonNull(value);
    }

    public static CategoryId newId() {
        return new CategoryId(UUID.randomUUID());
    }
}
