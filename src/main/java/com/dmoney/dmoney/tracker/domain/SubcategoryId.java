package com.dmoney.dmoney.tracker.domain;

import java.util.Objects;
import java.util.UUID;

public record SubcategoryId(UUID value) {
    public SubcategoryId {
        Objects.requireNonNull(value);
    }
}
