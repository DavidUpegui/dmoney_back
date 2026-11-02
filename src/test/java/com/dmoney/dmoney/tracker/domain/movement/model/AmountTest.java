package com.dmoney.dmoney.tracker.domain.movement.model;

import com.dmoney.dmoney.shared.domain.exceptions.ValidationException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AmountTest {

    @Test
    void mov02_accepts_positive_amount(){
        Amount amount = Amount.from(new BigDecimal("85.50"));

        assertEquals(new BigDecimal("85.50"), amount.value());
    }

    @Test
    void mov02_rejects_null(){
        assertThrows(ValidationException.class, () -> Amount.from(null));
    }

    @Test
    void mov02_rejects_zero(){
        assertThrows(ValidationException.class, () -> Amount.from(BigDecimal.ZERO));
    }

    @Test
    void mov02_rejects_negative(){
        assertThrows(ValidationException.class, () -> Amount.from(new BigDecimal("-1")));
    }
}
