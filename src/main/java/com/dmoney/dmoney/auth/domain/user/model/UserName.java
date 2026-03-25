package com.dmoney.dmoney.auth.domain.user.model;

import com.dmoney.dmoney.shared.domain.exceptions.ValidationException;

public record UserName(String value) {

    private static final int MAX_LENGTH = 100;

    public UserName(String value) {
        if (value == null || value.isBlank())
            throw new ValidationException("User name cannot be blank");
        String trimmed = value.trim();
        if (trimmed.length() > MAX_LENGTH)
            throw new ValidationException("User name is too long");
        this.value = trimmed;
    }

    public static UserName from(String name){
        return new UserName(name);
    }

    @Override
    public String toString(){
        return value;
    }
}
