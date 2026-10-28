package com.dmoney.dmoney.tracker.domain.movement.model;

import com.dmoney.dmoney.shared.domain.exceptions.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MovementDescriptionTest {

    @Test
    void shouldCreateValidDescription(){
        MovementDescription description = MovementDescription.from("Arriendo");

        assertEquals("Arriendo", description.value());
    }

    @Test
    void shouldTrimValue(){
        MovementDescription description = MovementDescription.from("   Arriendo   ");

        assertEquals("Arriendo", description.value());
    }

    @Test
    void shouldCreateEmptyDescription(){
        assertEquals("", MovementDescription.empty().value());
    }

    @Test
    void shouldReturnEmptyWhenNullableIsNull(){
        assertEquals("", MovementDescription.fromNullable(null).value());
    }

    @Test
    void shouldReturnValueWhenNullableIsPresent(){
        assertEquals("Arriendo", MovementDescription.fromNullable("Arriendo").value());
    }

    @Test
    void shouldThrowExceptionWhenValueIsNull(){
        assertThrows(ValidationException.class, () -> MovementDescription.from(null));
    }
}
