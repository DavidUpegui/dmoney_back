package com.dmoney.dmoney.tracker.domain.category;

import lombok.ToString;

import java.util.Objects;


public record Description(String value) {

    private static final int MAX_LENGTH = 255;

    public Description {
        Objects.requireNonNull(value, "Description cannot be null");
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("Description is too long");
        }
    }

    public static Description from(String value){
        return new Description(value);
    }

    public static Description fromNullable(String value){
        return value == null ? empty() : from(value);
    }

    public static Description empty(){
        return new Description("");
    }
}
