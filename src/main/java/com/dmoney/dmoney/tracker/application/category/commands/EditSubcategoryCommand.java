package com.dmoney.dmoney.tracker.application.category.commands;

import com.dmoney.dmoney.tracker.domain.category.CategoryId;
import com.dmoney.dmoney.tracker.domain.category.Description;
import com.dmoney.dmoney.tracker.domain.category.SubcategoryId;
import com.dmoney.dmoney.tracker.domain.category.SubcategoryName;

public record EditSubcategoryCommand(
        String categoryId,
        String subcategoryId,
        String subcategoryName,
        String subcategoryDescription) {
}
