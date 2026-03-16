package com.dmoney.dmoney.auth.application.user.register;

public record UserRegistrationCommand(
        String email,
        String password,
        String name
) {
}
