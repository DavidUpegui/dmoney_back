package com.dmoney.dmoney.tracker.application.movement.usecase;

import com.dmoney.dmoney.shared.domain.exceptions.ResourceNotFoundException;
import com.dmoney.dmoney.shared.domain.models.UserId;
import com.dmoney.dmoney.tracker.application.movement.response.MovementResponse;
import com.dmoney.dmoney.tracker.application.port.AuthenticatedUserProvider;
import com.dmoney.dmoney.tracker.domain.movement.model.Movement;
import com.dmoney.dmoney.tracker.domain.movement.model.MovementId;
import com.dmoney.dmoney.tracker.domain.movement.repository.MovementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindMovementByIdUseCase {

    private final MovementRepository movementRepository;
    private final AuthenticatedUserProvider authProvider;

    public MovementResponse execute(String id){
        UserId userId = authProvider.currentUserId();

        Movement movement = movementRepository.findByUserIdAndId(userId, MovementId.fromString(id))
                .orElseThrow(() -> new ResourceNotFoundException("Movement", "id", id));

        return MovementResponse.from(movement);
    }
}
