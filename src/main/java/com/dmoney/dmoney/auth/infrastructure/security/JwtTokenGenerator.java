package com.dmoney.dmoney.auth.infrastructure.security;

import com.dmoney.dmoney.auth.application.user.ports.JwtGenerator;
import com.dmoney.dmoney.shared.domain.models.UserId;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenGenerator implements JwtGenerator {

    private final SecretKey key;
    private static final long EXPIRATION = 86400000;

    public JwtTokenGenerator(@Value("${jwt.secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    @Override
    public String generate(UserId userId) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + EXPIRATION);

        return Jwts.builder()
                .subject(userId.value().toString())
                .issuedAt(now)
                .expiration(expiry)
                .signWith(key)
                .compact();
    }
}
