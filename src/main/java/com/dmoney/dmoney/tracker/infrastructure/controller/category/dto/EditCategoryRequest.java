package com.dmoney.dmoney.tracker.infrastructure.controller.category.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record  EditCategoryRequest(
        @Pattern(regexp = "^(?!\\s*$).+", message = "Name cannot be blank")
        String name,
        String description,
        String type,
        @Size(max = 48, message = "Icon is too long")
        String icon,
        @Size(max = 32, message = "Color is too long")
        String color
) {
}
