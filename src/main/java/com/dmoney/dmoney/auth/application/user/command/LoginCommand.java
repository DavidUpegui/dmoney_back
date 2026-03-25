package com.dmoney.dmoney.auth.application.user.command;

public record LoginCommand(
        String email,
        String password
) {
}
