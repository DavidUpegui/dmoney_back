package com.dmoney.dmoney.tracker.domain.movement.repository;

import com.dmoney.dmoney.shared.domain.models.UserId;
import com.dmoney.dmoney.tracker.domain.movement.model.Movement;
import com.dmoney.dmoney.tracker.domain.movement.model.MovementId;

import java.util.List;
import java.util.Optional;

public interface MovementRepository {
    Movement save(Movement movement);
    List<Movement> findAllByUserId(UserId userId);
    Optional<Movement> findByUserIdAndId(UserId userId, MovementId id);
    boolean deleteByUserIdAndId(UserId userId, MovementId id);
}
