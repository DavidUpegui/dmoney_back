package com.dmoney.dmoney.tracker.application.category.create;

import com.dmoney.dmoney.tracker.domain.category.CategoryId;
import com.dmoney.dmoney.tracker.domain.category.SubcategoryName;

public record AddSubcategoryCommand(
        CategoryId categoryId,
        SubcategoryName name,
        String description
) {
}
