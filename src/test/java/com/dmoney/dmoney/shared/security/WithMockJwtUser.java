package com.dmoney.dmoney.shared.security;

import org.springframework.security.test.context.support.WithSecurityContext;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockJwtUserFactory.class)
public @interface WithMockJwtUser {
    String userId() default "550e8400-e29b-41d4-a716-446655440000";
}

