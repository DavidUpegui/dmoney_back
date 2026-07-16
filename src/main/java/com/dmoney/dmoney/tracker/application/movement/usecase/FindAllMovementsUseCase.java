package com.dmoney.dmoney.tracker.application.movement.usecase;

import com.dmoney.dmoney.shared.domain.models.UserId;
import com.dmoney.dmoney.tracker.application.movement.response.MovementResponse;
import com.dmoney.dmoney.tracker.application.port.AuthenticatedUserProvider;
import com.dmoney.dmoney.tracker.domain.movement.repository.MovementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FindAllMovementsUseCase {

    private final MovementRepository movementRepository;
    private final AuthenticatedUserProvider authProvider;

    public List<MovementResponse> execute(){
        UserId userId = authProvider.currentUserId();
        return movementRepository.findAllByUserId(userId)
                .stream()
                .map(MovementResponse::from)
                .toList();
    }
}
