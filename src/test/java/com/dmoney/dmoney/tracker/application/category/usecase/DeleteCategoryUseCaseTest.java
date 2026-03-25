package com.dmoney.dmoney.tracker.application.category.usecase;

import com.dmoney.dmoney.shared.domain.models.UserId;
import com.dmoney.dmoney.tracker.application.port.AuthenticatedUserProvider;
import com.dmoney.dmoney.tracker.domain.category.model.CategoryId;
import com.dmoney.dmoney.tracker.domain.category.repository.CategoryRepository;
import com.dmoney.dmoney.shared.domain.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
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
    private CategoryRepository repository;

    @Mock
    private AuthenticatedUserProvider authProvider;

    @InjectMocks
    DeleteCategoryUseCase useCase;

    private UserId userId;

    @BeforeEach
    void setUp(){
        userId = UserId.newId();
        when(authProvider.currentUserId()).thenReturn(userId);
    }

    @Test
    void should_delete_category(){

        String categoryId = UUID.randomUUID().toString();

        when(repository.deleteByUserIdAndId(eq(userId), any()))
                .thenReturn(true);

        useCase.execute(categoryId);

        verify(repository).deleteByUserIdAndId(eq(userId), any(CategoryId.class));
    }

    @Test
    void should_throw_exception_when_category_is_not_found(){
        when(repository.deleteByUserIdAndId(eq(userId), any()))
                .thenReturn(false);

        String id = CategoryId.newId().value().toString();

        assertThrows(ResourceNotFoundException.class,
                ()-> useCase.execute(id));
    }
}
