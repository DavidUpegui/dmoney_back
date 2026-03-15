package com.dmoney.dmoney.auth.domain.user;

import java.util.Objects;
import java.util.UUID;

public record UserId(UUID value) {

    public UserId{
        Objects.requireNonNull(value,"" );
    }

    public static UserId fromString(String id){
        return new UserId(UUID.fromString(id));
    }

    public static UserId fromUUID(UUID id){
        return new UserId(id);
    }

    public static UserId newId(){
        return new UserId(UUID.randomUUID());
    }

    @Override
    public String toString(){
        return this.value.toString();
    }
}
