package com.dmoney.dmoney.tracker.infrastructure.controller.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCategoryRequest(
        @NotBlank(message = "Category name is required.")
        String name,
        String description,
        @NotBlank(message = "Category type is required.")
        String type,
        @Size(max = 48, message = "Icon is too long")
        String icon,
        @Size(max = 32, message = "Color is too long")
        String color
){}
