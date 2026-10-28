package com.dmoney.dmoney.tracker.application.movement.usecase;

import com.dmoney.dmoney.shared.domain.exceptions.ResourceNotFoundException;
import com.dmoney.dmoney.shared.domain.models.UserId;
import com.dmoney.dmoney.tracker.application.port.AuthenticatedUserProvider;
import com.dmoney.dmoney.tracker.domain.movement.model.MovementId;
import com.dmoney.dmoney.tracker.domain.movement.repository.MovementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteMovementUseCaseTest {

    @Mock
    private MovementRepository movementRepository;

    @Mock
    private AuthenticatedUserProvider authProvider;

    @InjectMocks
    private DeleteMovementUseCase useCase;

    private UserId userId;

    @BeforeEach
    void setUp(){
        userId = UserId.newId();
        when(authProvider.currentUserId()).thenReturn(userId);
    }

    @Test
    void should_delete_movement(){
        String id = MovementId.newId().value().toString();
        when(movementRepository.deleteByUserIdAndId(any(), any())).thenReturn(true);

        assertDoesNotThrow(() -> useCase.execute(id));

        verify(movementRepository).deleteByUserIdAndId(any(), any());
    }

    @Test
    void should_throw_when_movement_not_deleted(){
        String id = MovementId.newId().value().toString();
        when(movementRepository.deleteByUserIdAndId(any(), any())).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> useCase.execute(id));
    }
}
