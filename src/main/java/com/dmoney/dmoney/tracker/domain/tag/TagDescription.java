package com.dmoney.dmoney.tracker.domain.tag;

import java.util.Objects;

public record TagDescription(
        String value
) {
    private static final int MAX_LENGTH = 255;

    public TagDescription(String value){
        Objects.requireNonNull(value, "Tag description cannot be null");
        String normalized = value.trim();
        if(value.length() > MAX_LENGTH){
            throw new IllegalArgumentException("Tag description is too long");
        }
        this.value = normalized;
    }

    public static TagDescription from(String description){
        return new TagDescription(description);
    }

    public static TagDescription empty(){
        return new TagDescription("");
    }

    public static TagDescription fromNullable(String nullableDescription){
        return nullableDescription == null ?
                TagDescription.empty() : TagDescription.from(nullableDescription);
    }
}
