package com.dmoney.dmoney.auth.domain.user;

import java.util.Objects;

public record PasswordHash(String value) {
    public PasswordHash{
        Objects.requireNonNull(value, "Password hash cannot be null");

        if(value.isBlank()){
            throw new IllegalArgumentException("Password hash cannot be null");
        }
    }
    public static PasswordHash from(String value){
        return new PasswordHash(value);
    }

    @Override
    public String toString(){
        return this.value;
    }
}
