package com.dmoney.dmoney.tracker.application.category;

import com.dmoney.dmoney.tracker.application.category.result.CategoryResult;
import com.dmoney.dmoney.tracker.domain.category.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FindAllCategoriesUseCaseTest {

    @Mock
    CategoryRepository categoryRepository;

    @InjectMocks
    FindAllCategoriesUseCase useCase;

    @Test
    void should_return_all_categories_mapped_to_result() {
        // Arrange
        Category category1 = new Category(
                CategoryId.newId(),
                CategoryName.from("Food"),
                Description.from("Food category")
        );

        Category category2 = new Category(
                CategoryId.newId(),
                CategoryName.from("Tech"),
                Description.from("Tech category")
        );

        when(categoryRepository.findAll())
                .thenReturn(List.of(category1, category2));

        // Act
        List<CategoryResult> result = useCase.execute();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Food", result.get(0).name());
        assertEquals("Tech", result.get(1).name());

        verify(categoryRepository).findAll();
    }
    @Test
    void should_return_empty_list_when_no_categories_exist() {
        when(categoryRepository.findAll())
                .thenReturn(List.of());

        List<CategoryResult> result = useCase.execute();

        assertTrue(result.isEmpty());
        verify(categoryRepository).findAll();
    }
}
