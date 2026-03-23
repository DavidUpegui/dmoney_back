package com.dmoney.dmoney.auth.domain.user.model;

import com.dmoney.dmoney.shared.domain.exceptions.ValidationException;

public record PasswordHash(String value) {
    public PasswordHash {
        if (value == null || value.isBlank())
            throw new ValidationException("Password cannot be null or blank");
    }
    public static PasswordHash from(String value){
        return new PasswordHash(value);
    }

    @Override
    public String toString(){
        return this.value;
    }
}
