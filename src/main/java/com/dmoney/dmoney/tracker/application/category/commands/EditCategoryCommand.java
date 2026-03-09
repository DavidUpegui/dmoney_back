package com.dmoney.dmoney.tracker.application.category.commands;

import com.dmoney.dmoney.tracker.domain.category.CategoryId;
import com.dmoney.dmoney.tracker.domain.category.CategoryName;
import com.dmoney.dmoney.tracker.domain.category.Description;

public record EditCategoryCommand(
        String id,
        String name,
        String description
) {
}
