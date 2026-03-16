package com.dmoney.dmoney.auth.infrastructure.security;

import com.dmoney.dmoney.auth.application.user.ports.JwtGenerator;
import com.dmoney.dmoney.auth.domain.user.UserId;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenGenerator implements JwtGenerator {

    private final SecretKey key =
            Keys.hmacShaKeyFor("super-secret-key-super-secret-key".getBytes());

    private final static long EXPIRATION = 86400000;

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
