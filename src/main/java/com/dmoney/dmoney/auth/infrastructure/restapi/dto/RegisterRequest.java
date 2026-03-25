package com.dmoney.dmoney.auth.infrastructure.restapi.dto;

import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(
        @NotBlank
        String email,
        @NotBlank
        String password,
        @NotBlank
        String name
) {
}
