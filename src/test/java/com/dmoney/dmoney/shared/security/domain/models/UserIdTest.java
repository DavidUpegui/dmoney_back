package com.dmoney.dmoney.shared.security.domain.models;

import com.dmoney.dmoney.shared.domain.models.UserId;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserIdTest {

    @Test
    void should_create_user_id_from_uuid() {
        UUID uuid = UUID.randomUUID();
        UserId userId = UserId.fromUUID(uuid);
        assertEquals(uuid, userId.value());
    }

    @Test
    void should_create_user_id_from_string() {
        UUID uuid = UUID.randomUUID();
        UserId userId = UserId.fromString(uuid.toString());
        assertEquals(uuid, userId.value());
    }

    @Test
    void should_create_new_id() {
        UserId userId = UserId.newId();
        assertNotNull(userId.value());
    }

    @Test
    void should_throw_exception_when_null() {
        assertThrows(NullPointerException.class,
                () -> new UserId(null));
    }

    @Test
    void should_throw_exception_when_invalid_string() {
        assertThrows(IllegalArgumentException.class,
                () -> UserId.fromString("not-a-uuid"));
    }

    @Test
    void should_return_string_representation() {
        UUID uuid = UUID.randomUUID();
        UserId userId = UserId.fromUUID(uuid);
        assertEquals(uuid.toString(), userId.toString());
    }

    @Test
    void should_generate_unique_ids() {
        UserId id1 = UserId.newId();
        UserId id2 = UserId.newId();
        assertNotEquals(id1, id2);
    }
}
