package com.dmoney.dmoney.auth.infrastructure.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.UUID;

public class JwtTokenParser {

    private final SecretKey key =
            Keys.hmacShaKeyFor("super-secret-key-super-secret-key".getBytes());

    public UUID extractUserId(String token){
        String subject = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();

        return UUID.fromString(subject);
    }
}
