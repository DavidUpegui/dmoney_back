package com.dmoney.dmoney.shared.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import java.util.List;

public class WithMockJwtUserFactory
        implements WithSecurityContextFactory<WithMockJwtUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockJwtUser annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                        annotation.userId(),
                        null,
                        List.of()
                );
        context.setAuthentication(auth);
        return context;
    }
}
