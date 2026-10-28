package com.dmoney.dmoney.tracker.domain.movement.model;

import com.dmoney.dmoney.shared.domain.exceptions.ValidationException;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MovementIdTest {

    @Test
    void shouldGenerateNewId(){
        assertNotNull(MovementId.newId().value());
    }

    @Test
    void shouldCreateFromString(){
        UUID uuid = UUID.randomUUID();
        assertEquals(uuid, MovementId.fromString(uuid.toString()).value());
    }

    @Test
    void shouldCreateFromUUID(){
        UUID uuid = UUID.randomUUID();
        assertEquals(uuid, MovementId.fromUUID(uuid).value());
    }

    @Test
    void shouldThrowExceptionWhenValueIsNull(){
        assertThrows(ValidationException.class, () -> MovementId.fromUUID(null));
    }
}
