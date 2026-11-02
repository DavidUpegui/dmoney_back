package com.dmoney.dmoney.tracker.application.category.usecase;


import com.dmoney.dmoney.shared.domain.models.UserId;
import com.dmoney.dmoney.tracker.application.category.commands.DeleteSubcategoryCommand;
import com.dmoney.dmoney.tracker.application.category.helpers.CategoryLoader;
import com.dmoney.dmoney.shared.domain.exceptions.ResourceNotFoundException;
import com.dmoney.dmoney.shared.domain.exceptions.ResourceInUseException;
import com.dmoney.dmoney.tracker.application.port.AuthenticatedUserProvider;
import com.dmoney.dmoney.tracker.domain.category.model.*;
import com.dmoney.dmoney.tracker.domain.category.repository.CategoryRepository;
import com.dmoney.dmoney.tracker.domain.movement.repository.MovementRepository;
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

    @Mock
    private MovementRepository movementRepository;

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
    void cat16_deletes_subcategory(){
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
    void cat17_rejects_foreign_category_on_subcategory_delete(){
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
    void cat16_rejects_missing_subcategory(){

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

    @Test
    void cat16_rejects_delete_when_the_subcategory_has_movements(){
        Subcategory subcategory = category.addSubcategory(
                SubcategoryName.from("Subcategory name"),
                SubcategoryDescription.from("Subcategory description")
        );
        CategoryId categoryId = category.id();
        SubcategoryId subcategoryId = subcategory.id();
        DeleteSubcategoryCommand command = new DeleteSubcategoryCommand(
                categoryId.value().toString(),
                subcategoryId.value().toString()
        );

        when(categoryLoader.load(userId, categoryId)).thenReturn(category);
        when(movementRepository.existsByUserIdAndSubcategoryId(userId, subcategoryId))
                .thenReturn(true);

        assertThrows(ResourceInUseException.class, () -> useCase.execute(command));

        verify(categoryRepository, never()).save(any());
    }
}
