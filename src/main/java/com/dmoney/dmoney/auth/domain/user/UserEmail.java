package com.dmoney.dmoney.auth.domain.user;

import java.util.Objects;
import java.util.regex.Pattern;

public record UserEmail(String value) {
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    public UserEmail{
        Objects.requireNonNull(value);
        value = value.trim().toLowerCase();

        if(value().isBlank()){
            throw new IllegalArgumentException("Email cannot be blank");
        }

        if(!EMAIL_PATTERN.matcher(value).matches()){
            throw new IllegalArgumentException("Email cannot be blank");
        }
    }

    @Override
    public String toString() {
        return value;
    }

}
