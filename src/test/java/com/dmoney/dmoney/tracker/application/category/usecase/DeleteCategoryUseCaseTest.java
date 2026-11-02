package com.dmoney.dmoney.tracker.application.category.usecase;

import com.dmoney.dmoney.shared.domain.models.UserId;
import com.dmoney.dmoney.tracker.application.port.AuthenticatedUserProvider;
import com.dmoney.dmoney.tracker.domain.category.model.CategoryId;
import com.dmoney.dmoney.tracker.domain.category.repository.CategoryRepository;
import com.dmoney.dmoney.shared.domain.exceptions.ResourceInUseException;
import com.dmoney.dmoney.shared.domain.exceptions.ResourceNotFoundException;
import com.dmoney.dmoney.tracker.domain.movement.repository.MovementRepository;
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

    @Mock
    private MovementRepository movementRepository;

    @InjectMocks
    DeleteCategoryUseCase useCase;

    private UserId userId;

    @BeforeEach
    void setUp(){
        userId = UserId.newId();
        when(authProvider.currentUserId()).thenReturn(userId);
    }

    @Test
    void cat09_deletes_own_category(){

        String categoryId = UUID.randomUUID().toString();

        when(repository.deleteByUserIdAndId(eq(userId), any()))
                .thenReturn(true);

        useCase.execute(categoryId);

        verify(repository).deleteByUserIdAndId(eq(userId), any(CategoryId.class));
    }

    @Test
    void cat09_rejects_missing_category(){
        when(repository.deleteByUserIdAndId(eq(userId), any()))
                .thenReturn(false);

        String id = CategoryId.newId().value().toString();

        assertThrows(ResourceNotFoundException.class,
                ()-> useCase.execute(id));
    }

    @Test
    void cat11_rejects_delete_when_the_category_has_movements(){
        CategoryId categoryId = CategoryId.newId();

        when(movementRepository.existsByUserIdAndCategoryId(userId, categoryId))
                .thenReturn(true);

        assertThrows(ResourceInUseException.class,
                () -> useCase.execute(categoryId.value().toString()));

        verify(repository, never()).deleteByUserIdAndId(any(UserId.class), any(CategoryId.class));
    }
}
