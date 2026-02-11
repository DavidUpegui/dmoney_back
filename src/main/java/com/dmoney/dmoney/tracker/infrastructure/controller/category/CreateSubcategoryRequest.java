package com.dmoney.dmoney.tracker.infrastructure.controller.category;

public record CreateSubcategoryRequest(
        String categoryId,
        String name,
        String description
) {
}
