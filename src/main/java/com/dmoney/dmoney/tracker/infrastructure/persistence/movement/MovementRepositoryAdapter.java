package com.dmoney.dmoney.tracker.infrastructure.persistence.movement;

import com.dmoney.dmoney.shared.domain.models.UserId;
import com.dmoney.dmoney.tracker.domain.movement.model.Movement;
import com.dmoney.dmoney.tracker.domain.movement.model.MovementId;
import com.dmoney.dmoney.tracker.domain.movement.repository.MovementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MovementRepositoryAdapter implements MovementRepository {

    private final MovementJpaRepository jpaRepo;

    @Override
    public Movement save(Movement movement) {
        MovementEntity movementEntity = MovementPersistenceMapper.toEntity(movement);
        return MovementPersistenceMapper.toDomain(jpaRepo.save(movementEntity));
    }

    @Override
    public List<Movement> findAllByUserId(UserId userId) {
        return jpaRepo.findAllByUserId(userId.value()).stream().map(MovementPersistenceMapper::toDomain).toList();
    }

    @Override
    public Optional<Movement> findByUserIdAndId(UserId userId, MovementId id) {
        return jpaRepo.findByUserIdAndId(userId.value(), id.value())
                .map(MovementPersistenceMapper::toDomain);
    }

    @Override
    public boolean deleteByUserIdAndId(UserId userId, MovementId id) {
        int deleted = jpaRepo.deleteByUserIdAndId(userId.value(), id.value());
        return deleted > 0;
    }
}
