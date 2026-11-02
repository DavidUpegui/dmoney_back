package com.dmoney.dmoney.auth.infrastructure.security;

import com.dmoney.dmoney.shared.domain.models.UserId;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JwtTokenGeneratorTest {
    private final String secret = "test-secret-key-test-secret-key-test";
    private final JwtTokenGenerator generator = new JwtTokenGenerator(secret);
    private final JwtTokenParser parser = new JwtTokenParser(secret);

    @Test
    void auth07_generates_and_parses_token() {
        UserId userId = UserId.newId();

        String token = generator.generate(userId);
        UUID parsed = parser.extractUserId(token);

        assertEquals(userId.value(), parsed);
    }

    @Test
    void auth07_rejects_invalid_token() {
        assertThrows(Exception.class,
                () -> parser.extractUserId("token-invalido"));
    }

    @Test
    void auth07_token_expires_in_24_hours() {
        String token = generator.generate(UserId.newId());

        var claims = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload();

        assertEquals(
                86_400_000L,
                claims.getExpiration().getTime() - claims.getIssuedAt().getTime()
        );
    }
}
