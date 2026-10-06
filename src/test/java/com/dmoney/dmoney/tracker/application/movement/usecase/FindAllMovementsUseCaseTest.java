package com.dmoney.dmoney.tracker.application.movement.usecase;

import com.dmoney.dmoney.shared.domain.models.UserId;
import com.dmoney.dmoney.tracker.application.movement.response.MovementResponse;
import com.dmoney.dmoney.tracker.application.port.AuthenticatedUserProvider;
import com.dmoney.dmoney.tracker.domain.category.model.CategoryId;
import com.dmoney.dmoney.tracker.domain.category.model.SubcategoryId;
import com.dmoney.dmoney.tracker.domain.movement.model.Amount;
import com.dmoney.dmoney.tracker.domain.movement.model.Movement;
import com.dmoney.dmoney.tracker.domain.movement.model.MovementDescription;
import com.dmoney.dmoney.tracker.domain.movement.model.MovementFilter;
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
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindAllMovementsUseCaseTest {

    @Mock
    private MovementRepository movementRepository;

    @Mock
    private AuthenticatedUserProvider authProvider;

    @InjectMocks
    private FindAllMovementsUseCase useCase;

    private UserId userId;

    @BeforeEach
    void setUp(){
        userId = UserId.newId();
        when(authProvider.currentUserId()).thenReturn(userId);
    }

    @Test
    void should_forward_filter_and_return_user_movements(){
        Movement movement = Movement.create(
                userId,
                CategoryId.newId(),
                SubcategoryId.newId(),
                Amount.from(new BigDecimal("10")),
                MovementType.INCOME,
                MovementDescription.from("salary"),
                LocalDate.of(2026, 1, 1),
                Set.of()
        );

        MovementFilter filter = MovementFilter.of(null, null, null, null, null, null, null);

        when(movementRepository.findAllByUserId(eq(userId), eq(filter)))
                .thenReturn(List.of(movement));

        List<MovementResponse> responses = useCase.execute(filter);

        assertEquals(1, responses.size());
        assertEquals(movement.movementId().value().toString(), responses.getFirst().movementId());

        verify(movementRepository).findAllByUserId(eq(userId), eq(filter));
    }

    @Test
    void should_return_empty_list_when_no_movements_match(){
        MovementFilter filter = MovementFilter.of(null, null, null, null, null, null, null);

        when(movementRepository.findAllByUserId(eq(userId), eq(filter)))
                .thenReturn(List.of());

        List<MovementResponse> responses = useCase.execute(filter);

        assertEquals(0, responses.size());

        verify(movementRepository).findAllByUserId(eq(userId), eq(filter));
    }
}
