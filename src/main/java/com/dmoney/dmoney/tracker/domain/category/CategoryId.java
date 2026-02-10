package com.dmoney.dmoney.tracker.domain.category;

import java.util.Objects;
import java.util.UUID;

public class CategoryId {
    private final UUID value;

    public CategoryId(UUID value) {
        this.value = value;
    }

    public static CategoryId newId() {
        return new CategoryId(UUID.randomUUID());
    }

    public UUID value(){
        return this.value;
    }
}
