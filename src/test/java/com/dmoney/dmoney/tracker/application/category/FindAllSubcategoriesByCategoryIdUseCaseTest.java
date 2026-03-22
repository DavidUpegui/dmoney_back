package com.dmoney.dmoney.tracker.application.category;

import com.dmoney.dmoney.tracker.application.category.helpers.CategoryLoader;
import com.dmoney.dmoney.tracker.application.category.result.SubcategoryResult;
import com.dmoney.dmoney.shared.domain.exceptions.ResourceNotFoundException;
import com.dmoney.dmoney.tracker.application.category.usecase.FindAllSubcategoriesByCategoryIdUseCase;
import com.dmoney.dmoney.tracker.domain.category.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindAllSubcategoriesByCategoryIdUseCaseTest {

    @Mock
    CategoryLoader categoryLoader;

    @InjectMocks
    FindAllSubcategoriesByCategoryIdUseCase useCase;

    @Test
    void should_return_subcategories_from_category_id(){
        Category category = new Category(
                CategoryId.newId(),
                CategoryName.from("category name"),
                CategoryDescription.from("category description")
        );
        CategoryId categoryId = category.id();

        category.addSubcategory(
                SubcategoryName.from("Subcategory1 name"),
                CategoryDescription.from("Subcategory1 description")
        );

        category.addSubcategory(
                SubcategoryName.from("Subcategory2 name"),
                CategoryDescription.from("Subcategory2 description")
        );

        when(categoryLoader.load(categoryId))
                .thenReturn(category);

        List<SubcategoryResult> result = useCase.execute(categoryId.value().toString());

        verify(categoryLoader).load(categoryId);
        assertEquals(2, result.size());
        assertThat(result)
                .extracting(SubcategoryResult::subcategoryName)
                .containsExactlyInAnyOrder(
                        "Subcategory1 name",
                        "Subcategory2 name"
                );
    }

    @Test
    void should_return_void_when_no_subcategories_added(){
        Category category = new Category(
                CategoryId.newId(),
                CategoryName.from("category name"),
                CategoryDescription.from("category description")
        );
        CategoryId categoryId = category.id();

        when(categoryLoader.load(categoryId))
                .thenReturn(category);

        List<SubcategoryResult> result = useCase.execute(categoryId.value().toString());

        assertTrue(result.isEmpty());
        verify(categoryLoader).load(categoryId);
    }

    @Test
    void should_propagate_category_not_found_exception(){
        Category category = new Category(
                CategoryId.newId(),
                CategoryName.from("category name"),
                CategoryDescription.from("category description")
        );
        CategoryId categoryId = category.id();

        category.addSubcategory(
                SubcategoryName.from("Subcategory1 name"),
                CategoryDescription.from("Subcategory1 description")
        );

        when(categoryLoader.load(categoryId))
                .thenThrow(new ResourceNotFoundException(
                        "Category",
                        "id",
                        categoryId.value().toString()
                ));
        String id = categoryId.value().toString();
        assertThrows(ResourceNotFoundException.class,
                () -> useCase.execute(id));

        verify(categoryLoader).load(categoryId);
    }
}
