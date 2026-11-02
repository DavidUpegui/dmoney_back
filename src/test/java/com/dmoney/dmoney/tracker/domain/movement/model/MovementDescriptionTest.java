package com.dmoney.dmoney.tracker.domain.movement.model;

import com.dmoney.dmoney.shared.domain.exceptions.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MovementDescriptionTest {

    @Test
    void mov04_creates_description(){
        MovementDescription description = MovementDescription.from("Arriendo");

        assertEquals("Arriendo", description.value());
    }

    @Test
    void mov04_trims_description(){
        MovementDescription description = MovementDescription.from("   Arriendo   ");

        assertEquals("Arriendo", description.value());
    }

    @Test
    void mov04_creates_empty(){
        assertEquals("", MovementDescription.empty().value());
    }

    @Test
    void mov04_null_becomes_empty(){
        assertEquals("", MovementDescription.fromNullable(null).value());
    }

    @Test
    void mov04_creates_from_nullable(){
        assertEquals("Arriendo", MovementDescription.fromNullable("Arriendo").value());
    }

    @Test
    void mov04_rejects_null(){
        assertThrows(ValidationException.class, () -> MovementDescription.from(null));
    }
}
