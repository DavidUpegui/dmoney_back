package com.dmoney.dmoney.auth.domain.user.model;

import com.dmoney.dmoney.shared.domain.exceptions.ValidationException;

import java.util.Objects;
import java.util.regex.Pattern;

public record UserEmail(String value) {
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    public UserEmail(String value){
        if(value == null){
            throw new ValidationException("Email cannot be null.");
        }
        String normalized = value.trim().toLowerCase();

        if(normalized.isBlank()){
            throw new ValidationException("Email cannot be blank.");
        }

        if(!EMAIL_PATTERN.matcher(normalized).matches()){
            throw new ValidationException("Email doesn't match the correct format.");
        }
        this.value = normalized;
    }

    public static UserEmail from(String email){
        return new UserEmail(email);
    }

    @Override
    public String toString() {
        return value;
    }
}
