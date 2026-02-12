package com.dmoney.dmoney.tracker.application.category.commands;

import com.dmoney.dmoney.tracker.domain.category.CategoryId;
import com.dmoney.dmoney.tracker.domain.category.CategoryName;

public record EditCategoryCommand(
        CategoryId id,
        CategoryName name,
        String description
) {
}
