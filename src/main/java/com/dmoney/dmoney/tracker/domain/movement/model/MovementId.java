package com.dmoney.dmoney.tracker.domain.movement.model;

import com.dmoney.dmoney.shared.domain.exceptions.ValidationException;

import java.util.UUID;

public record MovementId(UUID value) {
    public MovementId(UUID value) {
        if (value == null) {
            throw new ValidationException("Movement id cannot be null.");
        }
        if (value.toString().isBlank()) {
            throw new ValidationException("Movement id cannot be blank.");
        }
        this.value = value;
    }

    public static MovementId fromString(String value) {
        return new MovementId(UUID.fromString(value));
    }

    public static MovementId fromUUID(UUID value) {
        return new MovementId(value);
    }

    public static MovementId newId(){
        return new MovementId(UUID.randomUUID());
    }
}
