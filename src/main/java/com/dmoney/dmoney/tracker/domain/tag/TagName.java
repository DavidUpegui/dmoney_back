package com.dmoney.dmoney.tracker.domain.tag;

import com.dmoney.dmoney.shared.domain.exceptions.ValidationException;

public record TagName(
        String value
) {
    public TagName(String value){
        if(value == null || value.isBlank()){
            throw new ValidationException("Tag name cannot be null or blank.");
        }
        this.value = value.trim();
    }

    public static TagName from(String name){
        return new TagName(name);
    }
}
