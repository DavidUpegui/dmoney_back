package com.dmoney.dmoney.tracker.domain;

import java.util.Objects;
import java.util.UUID;

public record CategoryId(UUID value) {
    public CategoryId {
        Objects.requireNonNull(value);
    }
}
