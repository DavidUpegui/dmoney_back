package com.dmoney.dmoney.tracker.infrastructure.controller.category.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateSubcategoryRequest(
        @NotBlank(message = "Subcategory name is required.")
        String name,
        String description
) {
}
