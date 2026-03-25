package com.dmoney.dmoney.tracker.application.category.usecase;

import com.dmoney.dmoney.shared.domain.models.UserId;
import com.dmoney.dmoney.tracker.application.category.helpers.CategoryLoader;
import com.dmoney.dmoney.tracker.application.category.result.SubcategoryResult;
import com.dmoney.dmoney.shared.domain.exceptions.ResourceNotFoundException;
import com.dmoney.dmoney.tracker.application.port.AuthenticatedUserProvider;
import com.dmoney.dmoney.tracker.domain.category.model.*;
import org.junit.jupiter.api.BeforeEach;
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
    private CategoryLoader categoryLoader;

    @Mock
    private AuthenticatedUserProvider authProvider;

    @InjectMocks
    private FindAllSubcategoriesByCategoryIdUseCase useCase;

    private UserId userId;

    @BeforeEach
    void setUp(){
        userId = UserId.newId();
        when(authProvider.currentUserId()).thenReturn(userId);
    }

    @Test
    void should_return_subcategories_from_category_id(){
        Category category = Category.create(
                userId,
                CategoryName.from("category name"),
                CategoryDescription.from("category description")
        );
        CategoryId categoryId = category.id();

        category.addSubcategory(
                SubcategoryName.from("Subcategory1 name"),
                SubcategoryDescription.from("Subcategory1 description")
        );

        category.addSubcategory(
                SubcategoryName.from("Subcategory2 name"),
                SubcategoryDescription.from("Subcategory2 description")
        );

        when(categoryLoader.load(userId, categoryId))
                .thenReturn(category);

        List<SubcategoryResult> result = useCase.execute(categoryId.value().toString());

        verify(categoryLoader).load(userId, categoryId);
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
        Category category = Category.create(
                userId,
                CategoryName.from("category name"),
                CategoryDescription.from("category description")
        );
        CategoryId categoryId = category.id();

        when(categoryLoader.load(userId, categoryId))
                .thenReturn(category);

        List<SubcategoryResult> result = useCase.execute(categoryId.value().toString());

        assertTrue(result.isEmpty());
        verify(categoryLoader).load(userId, categoryId);
    }

    @Test
    void should_propagate_category_not_found_exception() {
        CategoryId unexistingId = CategoryId.newId();

        when(categoryLoader.load(userId, unexistingId))
                .thenThrow(new ResourceNotFoundException(
                        "Category",
                        "id",
                        unexistingId.value().toString()
                ));

        assertThrows(ResourceNotFoundException.class,
                () -> useCase.execute(unexistingId.value().toString()));

        verify(categoryLoader).load(userId, unexistingId);
    }
}
