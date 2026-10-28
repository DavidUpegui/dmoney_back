package com.dmoney.dmoney.tracker.infrastructure.persistence.movement;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;
import java.util.UUID;

public interface MovementJpaRepository extends JpaRepository<MovementEntity, UUID>,
        JpaSpecificationExecutor<MovementEntity> {

    Optional<MovementEntity> findByUserIdAndId(UUID userId, UUID id);

    @Modifying(clearAutomatically = true)
    @Transactional
    int deleteByUserIdAndId(UUID userId, UUID id);
}
