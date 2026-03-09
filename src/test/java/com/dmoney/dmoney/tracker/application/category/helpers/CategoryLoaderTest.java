package com.dmoney.dmoney.tracker.application.category.helpers;

import com.dmoney.dmoney.tracker.domain.category.*;
import com.dmoney.dmoney.tracker.domain.exceptions.ResourceNotFoundException;
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
public class CategoryLoaderTest {

    @Mock
    CategoryRepository categoryRepository;

    @InjectMocks
    CategoryLoader categoryLoader;

    @Test
    void should_return_a_category(){
        CategoryId categoryId = CategoryId.newId();
        CategoryName categoryName = CategoryName.from("Category name");
        Description categoryDescription =  Description.from("Category description");

        Category category = new Category(categoryId, categoryName, categoryDescription);

        when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.of(category));

        Category result = categoryLoader.load(categoryId);

        assertEquals(categoryId, result.id());
        assertEquals(categoryName, result.name());
        assertEquals(categoryDescription, result.description());

        verify(categoryRepository).findById(categoryId);
    }

    @Test
    void should_propagate_category_not_found_exception(){
        CategoryId anyId = CategoryId.newId();

        when(categoryRepository.findById(anyId))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> categoryLoader.load(anyId));


        verify(categoryRepository).findById(anyId);
    }
}
