package com.dmoney.dmoney.tracker.infrastructure.controller.category;

import com.dmoney.dmoney.tracker.domain.category.Category;

public class CategoryWebMapper {

    public static CategoryResponse toResponse(Category cat){
        return new CategoryResponse(
                cat.id().value().toString(),
                cat.name().value(),
                cat.description()
        );
    }
}
