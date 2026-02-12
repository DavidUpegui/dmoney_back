package com.dmoney.dmoney.tracker.infrastructure.controller.category.dto;

import com.dmoney.dmoney.tracker.domain.category.CategoryId;
import com.dmoney.dmoney.tracker.domain.category.Subcategory;

public record SubcategoryResponse(
        String id,
        String name,
        String description,
        String categoryId
) {
    public static SubcategoryResponse from(
            Subcategory subcategory,
            CategoryId categoryId
    ) {
        return new SubcategoryResponse(
                subcategory.id().value().toString(),
                subcategory.name().value(),
                subcategory.description(),
                categoryId.value().toString()
        );

    }
}
