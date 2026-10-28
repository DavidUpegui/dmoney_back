package com.dmoney.dmoney.tracker.domain.movement.model;

import com.dmoney.dmoney.shared.domain.exceptions.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MovementTypeTest {

    @Test
    void shouldParseUppercase(){
        assertEquals(MovementType.INCOME, MovementType.from("INCOME"));
        assertEquals(MovementType.OUTCOME, MovementType.from("OUTCOME"));
        assertEquals(MovementType.TRANSFER, MovementType.from("TRANSFER"));
    }

    @Test
    void shouldParseCaseInsensitively(){
        assertEquals(MovementType.INCOME, MovementType.from("income"));
        assertEquals(MovementType.OUTCOME, MovementType.from("Outcome"));
        assertEquals(MovementType.TRANSFER, MovementType.from("tRaNsFeR"));
    }

    @Test
    void shouldThrowExceptionWhenValueIsNull(){
        assertThrows(ValidationException.class, () -> MovementType.from(null));
    }

    @Test
    void shouldThrowExceptionWhenValueIsInvalid(){
        assertThrows(ValidationException.class, () -> MovementType.from("FOO"));
    }
}
