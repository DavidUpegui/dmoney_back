package com.dmoney.dmoney.tracker.application.movement.usecase;

import com.dmoney.dmoney.shared.domain.exceptions.ResourceNotFoundException;
import com.dmoney.dmoney.shared.domain.models.UserId;
import com.dmoney.dmoney.tracker.application.movement.command.EditMovementCommand;
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
import com.dmoney.dmoney.tracker.domain.movement.service.MovementCategorizationValidator;
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
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EditMovementUseCaseTest {

    @Mock
    private MovementRepository movementRepository;

    @Mock
    private AuthenticatedUserProvider authProvider;

    @Mock
    private MovementCategorizationValidator categorizationValidator;

    @InjectMocks
    private EditMovementUseCase useCase;

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
        when(movementRepository.findByUserIdAndId(any(), any()))
                .thenReturn(Optional.of(movement));
        lenient().when(movementRepository.save(any(Movement.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void should_edit_amount_without_revalidating_categorization(){
        EditMovementCommand command = new EditMovementCommand(
                movement.movementId().value().toString(),
                null, null, new BigDecimal("99"), null, null, null, null
        );

        MovementResponse response = useCase.execute(command);

        assertEquals(new BigDecimal("99"), response.amount());

        verify(categorizationValidator, never()).validate(any(), any(), any(), any());
        verify(movementRepository).save(movement);
    }

    @Test
    void should_edit_category_and_revalidate_categorization(){
        String newCategoryId = CategoryId.newId().value().toString();

        EditMovementCommand command = new EditMovementCommand(
                movement.movementId().value().toString(),
                newCategoryId, null, null, null, null, null, null
        );

        MovementResponse response = useCase.execute(command);

        assertEquals(newCategoryId, response.categoryId());

        verify(categorizationValidator).validate(any(), any(), any(), any());
        verify(movementRepository).save(movement);
    }

    @Test
    void should_accept_lowercase_type(){
        EditMovementCommand command = new EditMovementCommand(
                movement.movementId().value().toString(),
                null, null, null, "outcome", null, null, null
        );

        MovementResponse response = useCase.execute(command);

        assertEquals("OUTCOME", response.type());
    }

    @Test
    void should_throw_when_movement_not_found(){
        when(movementRepository.findByUserIdAndId(any(), any()))
                .thenReturn(Optional.empty());

        EditMovementCommand command = new EditMovementCommand(
                MovementId.newId().value().toString(),
                null, null, null, null, null, null, null
        );

        assertThrows(ResourceNotFoundException.class, () -> useCase.execute(command));

        verify(movementRepository, never()).save(any(Movement.class));
    }
}
