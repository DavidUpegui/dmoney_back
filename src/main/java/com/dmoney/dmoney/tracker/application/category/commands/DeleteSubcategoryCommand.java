package com.dmoney.dmoney.tracker.application.category.commands;

import com.dmoney.dmoney.tracker.domain.category.CategoryId;
import com.dmoney.dmoney.tracker.domain.category.SubcategoryId;

public record DeleteSubcategoryCommand(
        CategoryId categoryId,
        SubcategoryId subcategoryId
) {
}
