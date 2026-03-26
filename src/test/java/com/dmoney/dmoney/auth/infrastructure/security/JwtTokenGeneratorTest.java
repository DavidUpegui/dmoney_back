package com.dmoney.dmoney.auth.infrastructure.security;

import com.dmoney.dmoney.shared.domain.models.UserId;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JwtTokenGeneratorTest {
    private final String secret = "test-secret-key-test-secret-key-test";
    private final JwtTokenGenerator generator = new JwtTokenGenerator(secret);
    private final JwtTokenParser parser = new JwtTokenParser(secret);

    @Test
    void should_generate_and_parse_token() {
        UserId userId = UserId.newId();

        String token = generator.generate(userId);
        UUID parsed = parser.extractUserId(token);

        assertEquals(userId.value(), parsed);
    }

    @Test
    void should_throw_exception_when_token_is_invalid() {
        assertThrows(Exception.class,
                () -> parser.extractUserId("token-invalido"));
    }
}
