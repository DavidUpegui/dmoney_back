package com.dmoney.dmoney.tracker.infrastructure.security;

import com.dmoney.dmoney.shared.domain.exceptions.UnauthenticatedException;
import com.dmoney.dmoney.shared.domain.models.UserId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SpringSecurityAuthProviderTest {

    private final SpringSecurityAuthProvider provider = new SpringSecurityAuthProvider();

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void should_return_user_id_when_authenticated() {
        UUID uuid = UUID.randomUUID();
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(uuid, null, List.of());
        SecurityContextHolder.getContext().setAuthentication(auth);

        UserId result = provider.currentUserId();

        assertEquals(UserId.fromUUID(uuid), result);
    }

    @Test
    void should_throw_exception_when_not_authenticated() {
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(null, null, List.of());
        auth.setAuthenticated(false);
        SecurityContextHolder.getContext().setAuthentication(auth);

        assertThrows(UnauthenticatedException.class,
                provider::currentUserId);
    }

    @Test
    void should_throw_exception_when_no_authentication() {
        SecurityContextHolder.clearContext();

        assertThrows(UnauthenticatedException.class,
                provider::currentUserId);
    }

    @Test
    void should_throw_exception_when_authentication_is_null() {
        SecurityContextHolder.getContext().setAuthentication(null);

        assertThrows(UnauthenticatedException.class,
                provider::currentUserId);
    }
}