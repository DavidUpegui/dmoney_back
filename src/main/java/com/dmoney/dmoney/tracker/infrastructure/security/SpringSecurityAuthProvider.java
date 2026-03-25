package com.dmoney.dmoney.tracker.infrastructure.security;

import com.dmoney.dmoney.shared.domain.exceptions.UnauthenticatedException;
import com.dmoney.dmoney.shared.domain.models.UserId;
import com.dmoney.dmoney.tracker.application.port.AuthenticatedUserProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SpringSecurityAuthProvider implements AuthenticatedUserProvider {
    @Override
    public UserId currentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new UnauthenticatedException("User not authenticated");
        }
        return UserId.fromUUID((UUID) auth.getPrincipal());
    }
}
