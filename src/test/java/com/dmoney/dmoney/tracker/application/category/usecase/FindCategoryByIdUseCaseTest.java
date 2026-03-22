package com.dmoney.dmoney.tracker.application.category.usecase;

import com.dmoney.dmoney.shared.domain.models.UserId;
import com.dmoney.dmoney.tracker.application.category.helpers.CategoryLoader;
import com.dmoney.dmoney.tracker.application.category.result.CategoryResult;
import com.dmoney.dmoney.shared.domain.exceptions.ResourceNotFoundException;
import com.dmoney.dmoney.tracker.application.port.AuthenticatedUserProvider;
import com.dmoney.dmoney.tracker.domain.category.model.Category;
import com.dmoney.dmoney.tracker.domain.category.model.CategoryDescription;
import com.dmoney.dmoney.tracker.domain.category.model.CategoryId;
import com.dmoney.dmoney.tracker.domain.category.model.CategoryName;
import org.junit.jupiter.api.BeforeEach;
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
class FindCategoryByIdUseCaseTest {

    @Mock
    private CategoryLoader categoryLoader;

    @Mock
    private AuthenticatedUserProvider authProvider;

    @InjectMocks
    FindCategoryByIdUseCase useCase;

    private UserId userId;

    @BeforeEach
    void setUp(){
        userId = UserId.newId();
        when(authProvider.currentUserId()).thenReturn(userId);
    }


    @Test
    void should_return_category_by_id(){
        CategoryName categoryName = CategoryName.from("Category name");
        CategoryDescription categoryDescription = CategoryDescription.from("Category description");
        CategoryId categoryId = CategoryId.newId();

        Category category = Category.create(userId, categoryName, categoryDescription);

        when(categoryLoader.load(userId, categoryId))
                .thenReturn(category);

        CategoryResult result = useCase.execute(categoryId.value().toString());

        assertEquals(categoryName.value(), result.name());
        assertEquals(categoryDescription.value(), result.description());
        assertEquals(categoryId.value().toString(), result.id());

        verify(categoryLoader).load(userId, categoryId);
    }

    @Test
    void should_propagate_category_not_found_exception(){
        CategoryId categoryId = CategoryId.newId();

        when(categoryLoader.load(userId, categoryId))
                .thenThrow(new ResourceNotFoundException(
                        "Category",
                        "id",
                        categoryId.value().toString()
                ));
        String id = categoryId.value().toString();
        assertThrows(ResourceNotFoundException.class,
                () -> useCase.execute(id));

        verify(categoryLoader).load(userId, categoryId);
    }
}
