package com.dmoney.dmoney.auth.infrastructure.restapi.dto;

public record LoginRequest(
        String email,
        String password
) {
}
