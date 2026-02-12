package com.dmoney.dmoney.tracker.infrastructure.controller.category;

public record  EditCategoryRequest(
        String id,
        String name,
        String description
) {
}
