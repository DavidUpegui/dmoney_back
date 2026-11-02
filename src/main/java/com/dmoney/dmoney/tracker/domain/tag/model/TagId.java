package com.dmoney.dmoney.tracker.domain.tag.model;

import com.dmoney.dmoney.shared.domain.exceptions.ValidationException;

import java.util.UUID;

public record TagId(
        UUID value
) {
    public TagId{
        if(value == null){
            throw new ValidationException("Tag id cannot be null.");
        }
    }

    public static TagId from(String id){
        if(id == null || id.isBlank()){
            throw new ValidationException("Invalid tag id: " + id);
        }
        try {
            return new TagId(UUID.fromString(id));
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Invalid tag id: " + id);
        }
    }

    public static TagId newId(){
        return new TagId(UUID.randomUUID());
    }
}
