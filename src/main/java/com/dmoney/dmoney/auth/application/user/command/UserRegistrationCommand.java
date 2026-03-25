package com.dmoney.dmoney.auth.application.user.command;

public record UserRegistrationCommand(
        String email,
        String password,
        String name
) {
}
