package com.dmoney.dmoney.tracker.infrastructure.controller.category;

import com.dmoney.dmoney.tracker.domain.category.Category;
import com.dmoney.dmoney.tracker.infrastructure.controller.category.dto.CategoryResponse;

public class CategoryWebMapper {

    public CategoryWebMapper(){}

    public static CategoryResponse toResponse(Category cat){
        return new CategoryResponse(
                cat.id().value().toString(),
                cat.name().value(),
                cat.description().value()
        );
    }
}
