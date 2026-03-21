package com.dmoney.dmoney.auth.application.user.ports;

import com.dmoney.dmoney.shared.domain.models.UserId;

public interface JwtGenerator {
    String generate(UserId userId);
}
