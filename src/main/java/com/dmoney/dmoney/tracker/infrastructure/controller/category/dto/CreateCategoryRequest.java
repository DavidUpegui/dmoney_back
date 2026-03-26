package com.dmoney.dmoney.tracker.infrastructure.controller.category.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateCategoryRequest(
        @NotBlank(message = "Category name is required.")
        String name,
        String description,
        @NotBlank(message = "Category type is required.")
        String type
){}
