package com.dmoney.dmoney.tracker.application.category;

import com.dmoney.dmoney.tracker.application.category.helpers.CategoryLoader;
import com.dmoney.dmoney.tracker.application.category.result.CategoryResult;
import com.dmoney.dmoney.tracker.domain.category.*;
import com.dmoney.dmoney.tracker.exceptions.CategoryNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FindCategoryByIdUseCaseTest {

    @Mock
    CategoryLoader categoryLoader;

    @InjectMocks
    FindCategoryByIdUseCase useCase;

    @Test
    void should_return_category_by_id(){
        CategoryName categoryName = CategoryName.from("Category name");
        Description categoryDescription = Description.from("Category description");
        CategoryId categoryId = CategoryId.newId();

        Category category = new Category(categoryId, categoryName, categoryDescription);

        when(categoryLoader.load(categoryId))
                .thenReturn(category);

        CategoryResult result = useCase.execute(categoryId.value().toString());

        assertEquals(categoryName.value(), result.name());
        assertEquals(categoryDescription.value(), result.description());
        assertEquals(categoryId.value().toString(), result.id());

        verify(categoryLoader).load(categoryId);
    }

    @Test
    void should_propagate_category_not_found_exception(){
        CategoryName categoryName = CategoryName.from("Category name");
        Description categoryDescription = Description.from("Category description");
        CategoryId categoryId = CategoryId.newId();

        Category category = new Category(categoryId, categoryName, categoryDescription);

        when(categoryLoader.load(categoryId))
                .thenThrow(new CategoryNotFoundException(
                        "id",
                        categoryId.value().toString()
                ));

        assertThrows(CategoryNotFoundException.class,
                () -> useCase.execute(categoryId.value().toString()));

        verify(categoryLoader).load(categoryId);
    }
}
