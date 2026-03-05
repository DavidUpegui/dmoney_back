package com.dmoney.dmoney.tracker.infrastructure.controller.category.dto;

import jakarta.validation.constraints.Pattern;

public record EditSubcategoryRequest(
        @Pattern(regexp = "^(?!\\s*$).+", message = "Name cannot be blank")
        String name,
        String description
) {
}
