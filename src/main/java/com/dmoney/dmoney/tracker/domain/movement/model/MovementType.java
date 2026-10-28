package com.dmoney.dmoney.tracker.domain.movement.model;

import com.dmoney.dmoney.shared.domain.exceptions.ValidationException;

public enum MovementType {
    INCOME,
    OUTCOME,
    TRANSFER;

    public static MovementType from(String value) {
        if (value == null) {
            throw new ValidationException("MovementType cannot be null");
        }

        try {
            return MovementType.valueOf(value.toUpperCase());
        } catch (Exception e) {
            throw new ValidationException("Invalid MovementType: " + value);
        }
    }
}
