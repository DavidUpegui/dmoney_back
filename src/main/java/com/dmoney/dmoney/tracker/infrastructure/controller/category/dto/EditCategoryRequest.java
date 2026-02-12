package com.dmoney.dmoney.tracker.infrastructure.controller.category.dto;

public record  EditCategoryRequest(
        String id,
        String name,
        String description
) {
}
