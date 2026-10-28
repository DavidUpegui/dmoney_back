package com.dmoney.dmoney.tracker.application.movement.usecase;

import com.dmoney.dmoney.shared.domain.exceptions.ResourceNotFoundException;
import com.dmoney.dmoney.shared.domain.models.UserId;
import com.dmoney.dmoney.tracker.application.movement.response.MovementResponse;
import com.dmoney.dmoney.tracker.application.port.AuthenticatedUserProvider;
import com.dmoney.dmoney.tracker.domain.category.model.CategoryId;
import com.dmoney.dmoney.tracker.domain.category.model.SubcategoryId;
import com.dmoney.dmoney.tracker.domain.movement.model.Amount;
import com.dmoney.dmoney.tracker.domain.movement.model.Movement;
import com.dmoney.dmoney.tracker.domain.movement.model.MovementDescription;
import com.dmoney.dmoney.tracker.domain.movement.model.MovementId;
import com.dmoney.dmoney.tracker.domain.movement.model.MovementType;
import com.dmoney.dmoney.tracker.domain.movement.repository.MovementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindMovementByIdUseCaseTest {

    @Mock
    private MovementRepository movementRepository;

    @Mock
    private AuthenticatedUserProvider authProvider;

    @InjectMocks
    private FindMovementByIdUseCase useCase;

    private UserId userId;
    private Movement movement;

    @BeforeEach
    void setUp(){
        userId = UserId.newId();
        movement = Movement.create(
                userId,
                CategoryId.newId(),
                SubcategoryId.newId(),
                Amount.from(new BigDecimal("10")),
                MovementType.INCOME,
                MovementDescription.from("salary"),
                LocalDate.of(2026, 1, 1),
                Set.of()
        );
        when(authProvider.currentUserId()).thenReturn(userId);
    }

    @Test
    void should_return_movement_when_found(){
        String id = movement.movementId().value().toString();
        when(movementRepository.findByUserIdAndId(any(), any()))
                .thenReturn(Optional.of(movement));

        MovementResponse response = useCase.execute(id);

        assertEquals(id, response.movementId());
        verify(movementRepository).findByUserIdAndId(userId, movement.movementId());
    }

    @Test
    void should_throw_when_movement_not_found(){
        String id = MovementId.newId().value().toString();
        when(movementRepository.findByUserIdAndId(any(), any()))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> useCase.execute(id));
    }
}
