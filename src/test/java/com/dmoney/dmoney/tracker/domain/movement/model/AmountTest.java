package com.dmoney.dmoney.tracker.domain.movement.model;

import com.dmoney.dmoney.shared.domain.exceptions.ValidationException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AmountTest {

    @Test
    void shouldCreateValidAmount(){
        Amount amount = Amount.from(new BigDecimal("85.50"));

        assertEquals(new BigDecimal("85.50"), amount.value());
    }

    @Test
    void shouldThrowExceptionWhenValueIsNull(){
        assertThrows(ValidationException.class, () -> Amount.from(null));
    }

    @Test
    void shouldThrowExceptionWhenValueIsZero(){
        assertThrows(ValidationException.class, () -> Amount.from(BigDecimal.ZERO));
    }

    @Test
    void shouldThrowExceptionWhenValueIsNegative(){
        assertThrows(ValidationException.class, () -> Amount.from(new BigDecimal("-1")));
    }
}
