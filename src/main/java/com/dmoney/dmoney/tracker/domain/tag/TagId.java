package com.dmoney.dmoney.tracker.domain.tag;

import java.util.Objects;
import java.util.UUID;

public record TagId(
        UUID value
) {
    public TagId(UUID value){
        this.value = Objects.requireNonNull(value, "Tag id cannot be null");
    }

    public static TagId from(String id){
        return new TagId(UUID.fromString(id));
    }

    public static TagId newId(){
        return new TagId(UUID.randomUUID());
    }
}
