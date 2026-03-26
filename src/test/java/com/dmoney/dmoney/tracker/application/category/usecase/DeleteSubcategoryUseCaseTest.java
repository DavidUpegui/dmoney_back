package com.dmoney.dmoney.tracker.application.category.usecase;


import com.dmoney.dmoney.shared.domain.models.UserId;
import com.dmoney.dmoney.tracker.application.category.commands.DeleteSubcategoryCommand;
import com.dmoney.dmoney.tracker.application.category.helpers.CategoryLoader;
import com.dmoney.dmoney.shared.domain.exceptions.ResourceNotFoundException;
import com.dmoney.dmoney.tracker.application.port.AuthenticatedUserProvider;
import com.dmoney.dmoney.tracker.domain.category.model.*;
import com.dmoney.dmoney.tracker.domain.category.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteSubcategoryUseCaseTest {

    @Mock
    CategoryLoader categoryLoader;

    @Mock
    CategoryRepository categoryRepository;

    @Mock
    private AuthenticatedUserProvider authProvider;

    @InjectMocks
    DeleteSubcategoryUseCase useCase;

    UserId userId;
    Category category;

    @BeforeEach
    void setUp(){
        userId = UserId.newId();
        category = Category.create(
                userId,
                CategoryName.from("category name"),
                CategoryDescription.from("category description"),
                CategoryType.INCOME
        );
        when(authProvider.currentUserId()).thenReturn(userId);
    }

    @Test
    void should_delete_subcategory_and_save_category(){
        Subcategory subcategory = category.addSubcategory(
                SubcategoryName.from("Subcategory name"),
                SubcategoryDescription.from("Subcategory description")
        );
        CategoryId categoryId = category.id();
        SubcategoryId subcategoryId = subcategory.id();

        DeleteSubcategoryCommand command =  new DeleteSubcategoryCommand(
                categoryId.value().toString(),
                subcategoryId.value().toString()
        );

        when(categoryLoader.load(userId, categoryId))
                .thenReturn(category);

        useCase.execute(command);

        verify(categoryRepository).save(category);
        verify(categoryLoader).load(userId, categoryId);

        assertTrue(category.subcategories().isEmpty());
    }

    @Test
    void should_propagate_category_not_found_exception(){
        CategoryId notExistingId = CategoryId.newId();
        DeleteSubcategoryCommand command =
                new DeleteSubcategoryCommand(
                        notExistingId.value().toString(),
                        UUID.randomUUID().toString()
                );

        when(categoryLoader.load(userId, notExistingId))
                .thenThrow(new ResourceNotFoundException("Category", "id", notExistingId.value().toString()));

        assertThrows(ResourceNotFoundException.class,
                () -> useCase.execute(command));

        verify(categoryLoader).load(userId, notExistingId);
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void should_propagate_subcategory_not_found_exception(){

        CategoryId categoryId = category.id();
        SubcategoryId notExistingSubcategoryId = SubcategoryId.newId();

        DeleteSubcategoryCommand command =
                new DeleteSubcategoryCommand(
                        categoryId.value().toString(),
                        notExistingSubcategoryId.value().toString()
                );

        when(categoryLoader.load(userId, categoryId))
                .thenReturn(category);

        assertThrows(ResourceNotFoundException.class,
                () -> useCase.execute(command));

        verify(categoryLoader).load(userId, categoryId);
        verify(categoryRepository, never()).save(any());
    }
}
