package com.dmoney.dmoney.tracker.application.category;

import com.dmoney.dmoney.tracker.application.category.commands.AddSubcategoryCommand;
import com.dmoney.dmoney.tracker.application.category.commands.CreateCategoryCommand;
import com.dmoney.dmoney.tracker.application.category.helpers.CategoryLoader;
import com.dmoney.dmoney.tracker.application.category.result.CategoryResult;
import com.dmoney.dmoney.tracker.application.category.result.SubcategoryResult;
import com.dmoney.dmoney.tracker.domain.category.*;
import com.dmoney.dmoney.tracker.exceptions.CategoryAlreadyExistsException;
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
public class CreateCategoryUseCaseTest {

    @Mock
    private CategoryRepository repository;

    @InjectMocks
    private CreateCategoryUseCase useCase;


    @Test
    void should_add_category_and_save() {

        CreateCategoryCommand command =
                new CreateCategoryCommand(
                        "new category",
                        "desc"
                );

        when(repository.existsByNameIgnoreCase(any()))
                .thenReturn(false);

        when(repository.save(any()))
                .thenAnswer(invocation ->
                        invocation.getArgument(0)
                );

        CategoryResult result = useCase.execute(command);


        verify(repository).existsByNameIgnoreCase(CategoryName.from("new category"));
        verify(repository).save(any(Category.class));

        assertEquals("new category", result.name());
    }

    @Test
    void should_throw_exception_when_category_already_exists(){
        CreateCategoryCommand categoryCommand =
                new CreateCategoryCommand("existing","desc");

        when(repository.existsByNameIgnoreCase(any()))
                .thenReturn(true);

        assertThrows(CategoryAlreadyExistsException.class,
                () -> useCase.execute(categoryCommand));

        verify(repository, never()).save(any());
        verify(repository).existsByNameIgnoreCase(CategoryName.from("existing"));
    }
}
