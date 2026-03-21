package com.dmoney.dmoney.tracker.domain.tag;

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
        return new TagId(UUID.fromString(id));
    }

    public static TagId newId(){
        return new TagId(UUID.randomUUID());
    }
}
