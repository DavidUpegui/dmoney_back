package com.dmoney.dmoney.auth.infrastructure.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BCryptPasswordHasherTest {
    private final BCryptPasswordHasher hasher = new BCryptPasswordHasher();

    @Test
    void should_hash_password() {
        String raw = "password123";

        String hashed = hasher.hash(raw);

        assertNotNull(hashed);
        assertNotEquals(raw, hashed);
    }

    @Test
    void should_match_password_with_hash() {
        String raw = "password123";
        String hashed = hasher.hash(raw);

        assertTrue(hasher.matches(raw, hashed));
    }

    @Test
    void should_not_match_wrong_password() {
        String raw = "password123";
        String hashed = hasher.hash(raw);

        assertFalse(hasher.matches("wrongpassword", hashed));
    }
}
