package com.dmoney.dmoney.tracker.application.category.commands;

public record EditCategoryCommand(
        String id,
        String name,
        String description,
        String type
) {
}
