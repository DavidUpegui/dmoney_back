package com.dmoney.dmoney.auth.application.user.ports;

import com.dmoney.dmoney.auth.domain.user.UserId;

public interface JwtGenerator {
    String generate(UserId userId);
}
