package com.dmoney.dmoney.tracker.application.movement.usecase;

import com.dmoney.dmoney.shared.domain.models.UserId;
import com.dmoney.dmoney.tracker.application.movement.command.CreateMovementCommand;
import com.dmoney.dmoney.tracker.application.movement.response.MovementResponse;
import com.dmoney.dmoney.tracker.application.port.AuthenticatedUserProvider;
import com.dmoney.dmoney.tracker.domain.movement.model.Movement;
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
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateMovementUseCaseTest {

    @Mock
    private MovementRepository movementRepository;

    @Mock
    private AuthenticatedUserProvider authProvider;

    @Mock
    private MovementCategorizationValidator categorizationValidator;

    @InjectMocks
    private CreateMovementUseCase useCase;

    private UserId userId;
    private String categoryId;
    private String subcategoryId;

    @BeforeEach
    void setUp(){
        userId = UserId.newId();
        categoryId = UUID.randomUUID().toString();
        subcategoryId = UUID.randomUUID().toString();
        when(authProvider.currentUserId()).thenReturn(userId);
        when(movementRepository.save(any(Movement.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
    }

    private CreateMovementCommand command(String type, String description){
        return new CreateMovementCommand(
                categoryId,
                subcategoryId,
                new BigDecimal("85.50"),
                type,
                description,
                LocalDate.of(2026, 7, 14),
                Set.of()
        );
    }

    @Test
    void should_create_movement_and_save(){
        MovementResponse response = useCase.execute(command("OUTCOME", "Arriendo"));

        assertEquals(new BigDecimal("85.50"), response.amount());
        assertEquals("OUTCOME", response.type());
        assertEquals("Arriendo", response.description());
        assertEquals(LocalDate.of(2026, 7, 14), response.date());

        verify(categorizationValidator).validate(any(), any(), any(), any());
        verify(movementRepository).save(any(Movement.class));
    }

    @Test
    void should_accept_lowercase_type(){
        MovementResponse response = useCase.execute(command("income", "salary"));

        assertEquals("INCOME", response.type());
    }

    @Test
    void should_accept_null_description(){
        MovementResponse response = useCase.execute(command("OUTCOME", null));

        assertEquals("", response.description());
    }
}
