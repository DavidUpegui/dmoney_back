package com.dmoney.dmoney.tracker.domain.movement;

import com.dmoney.dmoney.shared.domain.exceptions.ValidationException;

import java.math.BigDecimal;

public record Amount(BigDecimal value) {
    public Amount(BigDecimal value){
        if(value == null) {
            throw new ValidationException("Amount should be valid.");
        }
        if(value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Amount should be valid.");
        }
        this.value = value;
    }
}
