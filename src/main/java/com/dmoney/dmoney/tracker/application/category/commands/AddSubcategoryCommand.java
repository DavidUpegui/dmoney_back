package com.dmoney.dmoney.tracker.application.category.commands;

public record AddSubcategoryCommand(
        String categoryId,
        String name,
        String description
) {
}
