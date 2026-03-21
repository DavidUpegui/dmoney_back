package com.dmoney.dmoney.tracker.application.category;


import com.dmoney.dmoney.tracker.application.category.commands.EditCategoryCommand;
import com.dmoney.dmoney.tracker.application.category.helpers.CategoryLoader;
import com.dmoney.dmoney.tracker.application.category.result.CategoryResult;
import com.dmoney.dmoney.tracker.domain.category.*;
import com.dmoney.dmoney.shared.domain.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EditCategoryUseCaseTest {

    @Mock
    CategoryRepository categoryRepository;

    @Mock
    CategoryLoader categoryLoader;

    @InjectMocks
    EditCategoryUseCase useCase;

    Category category;

    @BeforeEach
    void setUp(){
        category = new Category(
                CategoryId.newId(),
                CategoryName.from("Category name"),
                Description.from("Category description")
        );
    }

    @Test
    void should_edit_and_save_the_category(){
        CategoryId categoryId = category.id();
        String newName ="New Category name";
        String newDescription = "New Category description";

        EditCategoryCommand command = new EditCategoryCommand(
                categoryId.value().toString(),
                newName,
                newDescription
        );

        when(categoryLoader.load(any()))
                .thenReturn(category);

        when(categoryRepository.save(any()))
                .thenAnswer(invocation ->
                        invocation.getArgument(0)
                );

        CategoryResult editedCategory = useCase.execute(command);


        verify(categoryLoader).load(categoryId);
        verify(categoryRepository).save(any(Category.class));
        assertEquals(newName, editedCategory.name());
        assertEquals(newDescription, editedCategory.description());
        assertEquals(categoryId.value().toString(), editedCategory.id());
    }

    @Test
    void should_only_edit_name_and_save_the_category(){
        CategoryId categoryId = category.id();
        String newName ="New Category name";

        EditCategoryCommand command = new EditCategoryCommand(
                categoryId.value().toString(),
                newName,
                null
        );

        when(categoryLoader.load(any()))
                .thenReturn(category);

        when(categoryRepository.save(any()))
                .thenAnswer(invocation ->
                        invocation.getArgument(0)
                );

        CategoryResult editedCategory = useCase.execute(command);


        verify(categoryLoader).load(categoryId);
        verify(categoryRepository).save(any(Category.class));

        assertEquals(newName, editedCategory.name());
        assertEquals(
                category.description().value(),
                editedCategory.description()
        );
        assertEquals(categoryId.value().toString(), editedCategory.id());
    }

    @Test
    void should_only_edit_description_and_save_the_category(){
        CategoryId categoryId = category.id();
        String newDescription = "New Category description";


        EditCategoryCommand command = new EditCategoryCommand(
                categoryId.value().toString(),
                null,
                newDescription
        );

        when(categoryLoader.load(any()))
                .thenReturn(category);

        when(categoryRepository.save(any()))
                .thenAnswer(invocation ->
                        invocation.getArgument(0)
                );

        CategoryResult editedCategory = useCase.execute(command);


        verify(categoryLoader).load(categoryId);
        verify(categoryRepository).save(any(Category.class));

        assertEquals(category.name().value(), editedCategory.name());
        assertEquals(newDescription, editedCategory.description());
        assertEquals(categoryId.value().toString(), editedCategory.id());
    }

    @Test
    void should_propagate_category_not_found_exception(){
        CategoryId unexistingId = CategoryId.newId();
        String newName ="New Category name";
        String newDescription = "New Category description";

        EditCategoryCommand command = new EditCategoryCommand(
                unexistingId.value().toString(),
                newName,
                newDescription
        );

        when(categoryLoader.load(any()))
                .thenThrow( new ResourceNotFoundException(
                        "Category",
                        "id",
                        unexistingId.toString()
                ));

        assertThrows(ResourceNotFoundException.class,
                () -> useCase.execute(command));

        verify(categoryLoader).load(unexistingId);
        verify(categoryRepository, never()).save(any());
    }
}
