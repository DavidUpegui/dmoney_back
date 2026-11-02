package com.dmoney.dmoney.tracker.domain.tag.model;

import com.dmoney.dmoney.shared.domain.exceptions.ValidationException;

public record TagName(
        String value
) {
    private static final int MAX_LENGTH = 255;

    public TagName(String value){
        if(value == null || value.isBlank()){
            throw new ValidationException("Tag name cannot be null or blank.");
        }
        String trimmed = value.trim();
        if(trimmed.length() > MAX_LENGTH){
            throw new ValidationException("Tag name is too long.");
        }
        this.value = trimmed;
    }

    public static TagName from(String name){
        return new TagName(name);
    }
}