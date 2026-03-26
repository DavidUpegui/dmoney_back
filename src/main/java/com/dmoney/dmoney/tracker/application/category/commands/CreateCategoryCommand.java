package com.dmoney.dmoney.tracker.application.category.commands;

public record CreateCategoryCommand(
        String name,
        String description,
        String type
) {
}
