package com.dmoney.dmoney.tracker.infrastructure.controller.category;


import com.dmoney.dmoney.tracker.domain.category.Category;
import com.dmoney.dmoney.tracker.domain.category.CategoryId;
import com.dmoney.dmoney.tracker.domain.category.CategoryName;
import com.dmoney.dmoney.tracker.domain.category.Description;
import com.dmoney.dmoney.tracker.infrastructure.controller.category.dto.CategoryResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CategoryWebMapperTest {
    @Test
    void should_transform_category_into_category_response(){
        Category domain  = new Category(
                CategoryId.newId(),
                CategoryName.from("Name"),
                Description.from("Description")
        );

        CategoryResponse result = CategoryWebMapper.toResponse(domain);

        assertEquals(domain.id().value().toString(), result.id());
        assertEquals(domain.name().value(), result.name());
        assertEquals(domain.description().value(), result.description());

    }
}
