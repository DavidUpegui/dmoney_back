package com.dmoney.dmoney.tracker.application.category.usecase;


import com.dmoney.dmoney.shared.domain.models.UserId;
import com.dmoney.dmoney.tracker.application.category.commands.AddSubcategoryCommand;
import com.dmoney.dmoney.tracker.application.category.helpers.CategoryLoader;
import com.dmoney.dmoney.tracker.application.category.result.SubcategoryResult;
import com.dmoney.dmoney.shared.domain.exceptions.ResourceNotFoundException;
import com.dmoney.dmoney.tracker.application.port.AuthenticatedUserProvider;
import com.dmoney.dmoney.tracker.domain.category.model.Category;
import com.dmoney.dmoney.tracker.domain.category.model.CategoryDescription;
import com.dmoney.dmoney.tracker.domain.category.model.CategoryName;
import com.dmoney.dmoney.tracker.domain.category.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddSubcategoryUseCaseTest {
    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryLoader categoryLoader;

    @Mock
    AuthenticatedUserProvider authProvider;

    @InjectMocks
    private AddSubcategoryUseCase useCase;

    private UserId userId;
    private Category category;

    @BeforeEach
    void setUp() {
        userId = UserId.newId();
        category = Category.create(
                userId,
                CategoryName.from("category"),
                CategoryDescription.from("description")
        );
    }

    @Test
    void should_add_subcategory_and_save_category() {

        AddSubcategoryCommand command =
                new AddSubcategoryCommand(
                        category.id().value().toString(),
                        "new subcategory",
                        "desc"
                );

        when(authProvider.currentUserId()).thenReturn(userId);

        when(categoryLoader.load(userId, category.id()))
                .thenReturn(category);

        SubcategoryResult result = useCase.execute(command);

        assertEquals("new subcategory",
                category.subcategories()
                        .iterator()
                        .next()
                        .name()
                        .value());

        verify(categoryRepository).save(category);
        assertEquals("new subcategory", result.subcategoryName());
    }

    @Test
    void should_throw_exception_when_category_is_not_found(){
        AddSubcategoryCommand command =
                new AddSubcategoryCommand(
                        category.id().value().toString(),
                        "new subcategory",
                        "desc"
                );
        when(authProvider.currentUserId()).thenReturn(userId);
        when(categoryLoader.load(userId, category.id()))
                .thenThrow(new ResourceNotFoundException(
                        "Category",
                        "id",
                        category.id().toString()
                ));

        assertThrows(ResourceNotFoundException.class,
                () ->  useCase.execute(command));

        verify(categoryLoader).load(userId, category.id());
        verify(categoryRepository, never()).save(category);
    }
}
