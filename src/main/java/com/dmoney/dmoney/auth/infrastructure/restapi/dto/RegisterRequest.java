package com.dmoney.dmoney.auth.infrastructure.restapi.dto;

public record RegisterRequest(
        String email,
        String password,
        String name
) {
}
