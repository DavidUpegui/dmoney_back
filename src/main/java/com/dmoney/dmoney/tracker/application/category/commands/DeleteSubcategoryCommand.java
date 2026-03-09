package com.dmoney.dmoney.tracker.application.category.commands;


public record DeleteSubcategoryCommand(
        String categoryId,
        String subcategoryId
) {
}
