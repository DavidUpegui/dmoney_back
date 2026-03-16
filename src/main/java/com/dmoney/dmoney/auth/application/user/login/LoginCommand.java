package com.dmoney.dmoney.auth.application.user.login;

public record LoginCommand(
        String email,
        String password
) {
}
