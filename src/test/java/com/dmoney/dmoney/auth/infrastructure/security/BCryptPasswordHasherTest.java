package com.dmoney.dmoney.auth.infrastructure.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BCryptPasswordHasherTest {
    private final BCryptPasswordHasher hasher = new BCryptPasswordHasher();

    @Test
    void auth04_hashes_password() {
        String raw = "password123";

        String hashed = hasher.hash(raw);

        assertNotNull(hashed);
        assertNotEquals(raw, hashed);
    }

    @Test
    void auth04_matches_password() {
        String raw = "password123";
        String hashed = hasher.hash(raw);

        assertTrue(hasher.matches(raw, hashed));
    }

    @Test
    void auth04_rejects_wrong_password() {
        String raw = "password123";
        String hashed = hasher.hash(raw);

        assertFalse(hasher.matches("wrongpassword", hashed));
    }
}
