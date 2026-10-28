package com.dmoney.dmoney.tracker.application.movement.usecase;

import com.dmoney.dmoney.shared.domain.exceptions.ResourceNotFoundException;
import com.dmoney.dmoney.shared.domain.models.UserId;
import com.dmoney.dmoney.tracker.application.port.AuthenticatedUserProvider;
import com.dmoney.dmoney.tracker.domain.movement.model.MovementId;
import com.dmoney.dmoney.tracker.domain.movement.repository.MovementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteMovementUseCase {

    private final MovementRepository movementRepository;
    private final AuthenticatedUserProvider authProvider;

    public void execute(String id){
        UserId userId = authProvider.currentUserId();
        MovementId movementId = MovementId.fromString(id);

        boolean deleted = movementRepository.deleteByUserIdAndId(userId, movementId);
        if(!deleted){
            throw new ResourceNotFoundException("Movement", "id", id);
        }
    }
}
