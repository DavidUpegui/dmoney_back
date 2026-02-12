package com.dmoney.dmoney.tracker.infrastructure.controller.category.dto;

public record CreateSubcategoryRequest(
        String categoryId,
        String name,
        String description
) {
}
