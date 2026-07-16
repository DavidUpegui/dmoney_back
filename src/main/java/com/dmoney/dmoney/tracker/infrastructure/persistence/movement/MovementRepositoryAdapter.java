package com.dmoney.dmoney.tracker.infrastructure.persistence.movement;

import com.dmoney.dmoney.tracker.domain.movement.model.Movement;
import com.dmoney.dmoney.tracker.domain.movement.repository.MovementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MovementRepositoryAdapter implements MovementRepository {

    private final MovementJpaRepository jpaRepo;

    @Override
    public Movement save(Movement movement) {
        MovementEntity movementEntity = MovementPersistenceMapper.toEntity(movement);
        return MovementPersistenceMapper.toDomain(jpaRepo.save(movementEntity));
    }
}
