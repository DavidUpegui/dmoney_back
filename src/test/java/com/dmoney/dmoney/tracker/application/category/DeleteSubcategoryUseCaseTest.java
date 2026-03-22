package com.dmoney.dmoney.tracker.application.category;


import com.dmoney.dmoney.tracker.application.category.commands.DeleteSubcategoryCommand;
import com.dmoney.dmoney.tracker.application.category.helpers.CategoryLoader;
import com.dmoney.dmoney.shared.domain.exceptions.ResourceNotFoundException;
import com.dmoney.dmoney.tracker.application.category.usecase.DeleteSubcategoryUseCase;
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

    @InjectMocks
    DeleteSubcategoryUseCase useCase;

    Category category;

    @BeforeEach
    void setUp(){
        category = new Category(
                CategoryId.newId(),
                CategoryName.from("category name"),
                CategoryDescription.from("category description")
        );
    }

    @Test
    void should_delete_subcategory_and_save_category(){
        Subcategory subcategory = category.addSubcategory(
                SubcategoryName.from("Subcategory name"),
                CategoryDescription.from("Subcategory description")
        );
        CategoryId categoryId = category.id();
        SubcategoryId subcategoryId = subcategory.id();

        DeleteSubcategoryCommand command =  new DeleteSubcategoryCommand(
                categoryId.value().toString(),
                subcategoryId.value().toString()
        );

        when(categoryLoader.load((any())))
                .thenReturn(category);

        useCase.execute(command);

        verify(categoryRepository).save(category);
        verify(categoryLoader).load(categoryId);

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

        when(categoryLoader.load(notExistingId))
                .thenThrow(new ResourceNotFoundException("Category", "id", notExistingId.value().toString()));

        assertThrows(ResourceNotFoundException.class,
                () -> useCase.execute(command));

        verify(categoryLoader).load(notExistingId);
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

        when(categoryLoader.load(categoryId))
                .thenReturn(category);

        assertThrows(ResourceNotFoundException.class,
                () -> useCase.execute(command));

        verify(categoryLoader).load(categoryId);
        verify(categoryRepository, never()).save(any());
    }
}
