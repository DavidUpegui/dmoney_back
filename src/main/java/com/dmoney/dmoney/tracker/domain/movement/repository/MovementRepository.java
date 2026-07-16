package com.dmoney.dmoney.tracker.domain.movement.repository;

import com.dmoney.dmoney.tracker.domain.movement.model.Movement;

public interface MovementRepository {
    Movement save(Movement movement);
}
