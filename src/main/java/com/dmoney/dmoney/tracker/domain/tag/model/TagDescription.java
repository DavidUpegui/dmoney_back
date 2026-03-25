package com.dmoney.dmoney.tracker.domain.tag.model;

import com.dmoney.dmoney.shared.domain.exceptions.ValidationException;

public record TagDescription(
        String value
) {
    private static final int MAX_LENGTH = 255;

    public TagDescription(String value){
        String normalized = value != null ? value.trim() : "";
        if(normalized.length() > MAX_LENGTH){
            throw new ValidationException("Tag description is too long");
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
