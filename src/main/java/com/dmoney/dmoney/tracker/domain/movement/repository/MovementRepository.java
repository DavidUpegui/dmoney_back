package com.dmoney.dmoney.tracker.domain.movement.repository;

import com.dmoney.dmoney.shared.domain.models.UserId;
import com.dmoney.dmoney.tracker.domain.movement.model.Movement;

import java.util.List;

public interface MovementRepository {
    Movement save(Movement movement);
    List<Movement> findAllByUserId(UserId userId);
}
