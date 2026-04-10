package com.dmoney.dmoney.tracker.domain.movement;

import com.dmoney.dmoney.shared.domain.exceptions.ValidationException;

public record MovementDescription(String value) {
    public MovementDescription(String value){
        if(value == null){
            throw new ValidationException("Movement description cannot be null.");
        }
        this.value = value.trim();
    }

    public static MovementDescription from(String value) {
        return new MovementDescription(value);
    }

    public static MovementDescription fromNullable(String value){
        return value == null ? empty(): from(value);
    }

    public static MovementDescription empty(){
        return new MovementDescription("");
    }
}
