package com.dmoney.dmoney.tracker.application.category;

import com.dmoney.dmoney.tracker.application.category.commands.EditSubcategoryCommand;
import com.dmoney.dmoney.tracker.application.category.helpers.CategoryLoader;
import com.dmoney.dmoney.tracker.application.category.result.SubcategoryResult;
import com.dmoney.dmoney.shared.domain.exceptions.ResourceNotFoundException;
import com.dmoney.dmoney.tracker.application.category.usecase.EditSubcategoryUseCase;
import com.dmoney.dmoney.tracker.domain.category.model.*;
import com.dmoney.dmoney.tracker.domain.category.repository.CategoryRepository;
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
class EditSubcategoryUseCaseTest {
    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryLoader categoryLoader;

    @InjectMocks
    private EditSubcategoryUseCase editSubcategoryUseCase;

    private Category category;
    private Subcategory subcategory;

    @BeforeEach
    void setUp(){
        category = new Category(
                CategoryId.newId(),
                CategoryName.from("Category name"),
                CategoryDescription.from("Category description")
        );
        subcategory = category.addSubcategory(
                SubcategoryName.from("Subcategory name"),
                CategoryDescription.from("Subcategory description")
        );
    }

    @Test
    void should_edit_subcategory_and_save_category(){
        CategoryId categoryId = category.id();
        SubcategoryId subcategoryId = subcategory.id();

        String newSubcategoryName = "New name";
        String newSubcategoryDescription = "New description";

        EditSubcategoryCommand command = new EditSubcategoryCommand(
                categoryId.value().toString(),
                subcategoryId.value().toString(),
                newSubcategoryName,
                newSubcategoryDescription
        );

        when(categoryLoader.load(any()))
                .thenReturn(category);

        when(categoryRepository.save(any()))
                .thenAnswer( invocation ->
                        invocation.getArgument(0)
                );

        SubcategoryResult subcategoryResult =
                editSubcategoryUseCase.execute(command);

        assertEquals(newSubcategoryName, subcategoryResult.subcategoryName());
        assertEquals(newSubcategoryDescription, subcategoryResult.subcategoryDescription());

        verify(categoryLoader).load(categoryId);
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void should_edit_only_subcategory_name_and_save_category(){
        CategoryId categoryId = category.id();
        SubcategoryId subcategoryId = subcategory.id();

        String newSubcategoryName = "New name";
        String originalSubcategoryDescription =
                subcategory.description().value();

        EditSubcategoryCommand command = new EditSubcategoryCommand(
                categoryId.value().toString(),
                subcategoryId.value().toString(),
                newSubcategoryName,
                null
        );

        when(categoryLoader.load(any()))
                .thenReturn(category);

        when(categoryRepository.save(any()))
                .thenAnswer( invocation ->
                        invocation.getArgument(0)
                );

        SubcategoryResult subcategoryResult =
                editSubcategoryUseCase.execute(command);

        assertEquals(newSubcategoryName, subcategoryResult.subcategoryName());
        assertEquals(originalSubcategoryDescription, subcategoryResult.subcategoryDescription());

        verify(categoryLoader).load(categoryId);
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void should_edit_only_subcategory_description_and_save_category(){
        CategoryId categoryId = category.id();
        SubcategoryId subcategoryId = subcategory.id();

        String originalSubcategoryName = subcategory.name().value();
        String newSubcategoryDescription = "New description";

        EditSubcategoryCommand command = new EditSubcategoryCommand(
                categoryId.value().toString(),
                subcategoryId.value().toString(),
                null,
                newSubcategoryDescription
        );

        when(categoryLoader.load(any()))
                .thenReturn(category);

        when(categoryRepository.save(any()))
                .thenAnswer( invocation ->
                        invocation.getArgument(0)
                );

        SubcategoryResult subcategoryResult =
                editSubcategoryUseCase.execute(command);

        assertEquals(originalSubcategoryName, subcategoryResult.subcategoryName());
        assertEquals(newSubcategoryDescription, subcategoryResult.subcategoryDescription());

        verify(categoryLoader).load(categoryId);
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void should_propagate_category_not_found_exception(){
        CategoryId anyId = CategoryId.newId();
        SubcategoryId subcategoryId = subcategory.id();

        String newSubcategoryName = "New name";
        String newSubcategoryDescription = "New description";

        EditSubcategoryCommand command = new EditSubcategoryCommand(
                anyId.value().toString(),
                subcategoryId.value().toString(),
                newSubcategoryName,
                newSubcategoryDescription
        );

        when(categoryLoader.load(any()))
                .thenThrow(new ResourceNotFoundException(
                        "Category",
                        "id",
                        anyId.value().toString()
                ));

        assertThrows(ResourceNotFoundException.class,
                () -> editSubcategoryUseCase.execute(command));

        verify(categoryLoader).load(anyId);
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void should_propagate_subcategory_not_found_exception(){
        CategoryId categoryId = category.id();
        SubcategoryId anyId = SubcategoryId.newId();

        String newSubcategoryName = "New name";
        String newSubcategoryDescription = "New description";

        EditSubcategoryCommand command = new EditSubcategoryCommand(
                categoryId.value().toString(),
                anyId.value().toString(),
                newSubcategoryName,
                newSubcategoryDescription
        );

        when(categoryLoader.load(any()))
                .thenReturn(category);

        assertThrows(ResourceNotFoundException.class,
                () -> editSubcategoryUseCase.execute(command));

        verify(categoryLoader).load(categoryId);
        verify(categoryRepository, never()).save(any());
    }
}
