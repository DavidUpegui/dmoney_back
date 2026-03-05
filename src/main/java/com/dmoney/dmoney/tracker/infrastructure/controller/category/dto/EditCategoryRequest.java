package com.dmoney.dmoney.tracker.infrastructure.controller.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record  EditCategoryRequest(
        @NotBlank(message = "Category id is required.")
        String id,
        @Pattern(regexp = "^(?!\\s*$).+", message = "Name cannot be blank")
        String name,
        String description
) {
}
