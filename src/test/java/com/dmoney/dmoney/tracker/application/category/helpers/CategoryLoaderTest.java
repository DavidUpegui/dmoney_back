package com.dmoney.dmoney.tracker.application.category.helpers;

import com.dmoney.dmoney.shared.domain.exceptions.ResourceNotFoundException;
import com.dmoney.dmoney.shared.domain.models.UserId;
import com.dmoney.dmoney.tracker.domain.category.model.*;
import com.dmoney.dmoney.tracker.domain.category.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryLoaderTest {

    @Mock
    CategoryRepository categoryRepository;

    @InjectMocks
    CategoryLoader categoryLoader;

    @Test
    void should_return_a_category(){
        UserId userId = UserId.newId();
        CategoryName categoryName = CategoryName.from("Category name");
        CategoryDescription categoryDescription =  CategoryDescription.from("Category description");
        CategoryType type = CategoryType.INCOME;

        Category category = Category.create(userId, categoryName, categoryDescription, type);
        CategoryId categoryId = category.id();
        when(categoryRepository.findByUserIdAndId(userId, categoryId))
                .thenReturn(Optional.of(category));

        Category result = categoryLoader.load(userId, categoryId);

        assertEquals(categoryId, result.id());
        assertEquals(categoryName, result.name());
        assertEquals(categoryDescription, result.description());
        assertEquals(type, result.categoryType());

        verify(categoryRepository).findByUserIdAndId(userId, categoryId);
    }

    @Test
    void should_propagate_category_not_found_exception(){
        UserId userId = UserId.newId();
        CategoryId anyId = CategoryId.newId();

        when(categoryRepository.findByUserIdAndId(userId, anyId))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> categoryLoader.load(userId, anyId));


        verify(categoryRepository).findByUserIdAndId(userId, anyId);
    }
}
