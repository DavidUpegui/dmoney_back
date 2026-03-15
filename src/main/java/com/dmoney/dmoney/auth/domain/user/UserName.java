package com.dmoney.dmoney.auth.domain.user;

import java.util.Objects;

public record UserName(String value) {

    private static final int MAX_LENGTH = 100;

    public UserName{
        Objects.requireNonNull(value);
        value = value.trim();
        if(value.isBlank()){
            throw new IllegalArgumentException("User name cannot be blank");
        }
        if(value.length() > MAX_LENGTH){
            throw new IllegalArgumentException("User name is too long");
        }
    }

    public static UserName from(String name){
        return new UserName(name);
    }

    @Override
    public String toString(){
        return value;
    }
}
