package com.dmoney.dmoney.tracker.application.category.commands;

import com.dmoney.dmoney.tracker.domain.category.CategoryId;
import com.dmoney.dmoney.tracker.domain.category.Description;
import com.dmoney.dmoney.tracker.domain.category.SubcategoryName;

public record AddSubcategoryCommand(
        String categoryId,
        String name,
        String description
) {
}
