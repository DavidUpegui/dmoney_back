package com.dmoney.dmoney.tracker.infrastructure.controller.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateSubcategoryRequest(
        @NotBlank(message = "Subcategory name is required.")
        String name,
        String description,
        @Size(max = 48, message = "Icon is too long")
        String icon,
        @Size(max = 32, message = "Color is too long")
        String color
) {
}
