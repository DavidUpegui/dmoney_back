package com.dmoney.dmoney.tracker.application.category;

import com.dmoney.dmoney.tracker.domain.category.CategoryId;
import com.dmoney.dmoney.tracker.domain.category.CategoryRepository;
import com.dmoney.dmoney.tracker.domain.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class DeleteCategoryUseCaseTest {

    @Mock
    CategoryRepository repository;

    @InjectMocks
    DeleteCategoryUseCase useCase;

    @Test
    void should_delete_category(){

        String categoryId = UUID.randomUUID().toString();

        when(repository.existsById(any()))
                .thenReturn(true);

        useCase.execute(categoryId);

        verify(repository).delete(CategoryId.from(categoryId));
        verify(repository).existsById(CategoryId.from(categoryId));
    }

    @Test
    void should_throw_exception_when_category_is_not_found(){
        when(repository.existsById(any()))
                .thenReturn(false);

        String id = CategoryId.newId().value().toString();

        assertThrows(ResourceNotFoundException.class,
                ()-> useCase.execute(id));

        verify(repository, never()).delete(any());
    }
}
