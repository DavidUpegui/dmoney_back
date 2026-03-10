package com.dmoney.dmoney.tracker.infrastructure.controller.tag.dto;

import jakarta.validation.constraints.NotBlank;

public record TagCreationRequest(
        @NotBlank(message = "Tag name cannot be blank or null")
        String name,
        String description
) {
}
