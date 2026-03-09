package com.dmoney.dmoney.tracker.application.category.commands;

public record EditSubcategoryCommand(
        String categoryId,
        String subcategoryId,
        String subcategoryName,
        String subcategoryDescription) {
}
