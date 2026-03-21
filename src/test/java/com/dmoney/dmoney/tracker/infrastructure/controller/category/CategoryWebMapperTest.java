package com.dmoney.dmoney.tracker.infrastructure.controller.category;


import com.dmoney.dmoney.tracker.domain.category.Category;
import com.dmoney.dmoney.tracker.domain.category.CategoryId;
import com.dmoney.dmoney.tracker.domain.category.CategoryName;
import com.dmoney.dmoney.tracker.domain.category.CategoryDescription;
import com.dmoney.dmoney.tracker.infrastructure.controller.category.dto.CategoryResponse;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CategoryWebMapperTest {
    @Test
    void should_transform_category_into_category_response(){
        Category domain  = new Category(
                CategoryId.newId(),
                CategoryName.from("Name"),
                CategoryDescription.from("Description")
        );

        CategoryResponse result = CategoryWebMapper.toResponse(domain);

        assertEquals(domain.id().value().toString(), result.id());
        assertEquals(domain.name().value(), result.name());
        assertEquals(domain.description().value(), result.description());
    }

    @Test
    void shouldHavePrivateConstructor() throws Exception {
        Constructor<CategoryWebMapper> constructor =
                CategoryWebMapper.class.getDeclaredConstructor();

        assertTrue(java.lang.reflect.Modifier.isPrivate(constructor.getModifiers()));

        constructor.setAccessible(true);
        constructor.newInstance();
    }
}
