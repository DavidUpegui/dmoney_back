package com.dmoney.dmoney.tracker.infrastructure.persistence.movement;

import com.dmoney.dmoney.shared.domain.models.UserId;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MovementJpaRepository extends JpaRepository<MovementEntity, UUID> {
    List<MovementEntity> findAllByUserId(UUID userId);

    Optional<MovementEntity> findByUserIdAndId(UUID userId, UUID id);

    @Modifying(clearAutomatically = true)
    @Transactional
    int deleteByUserIdAndId(UUID userId, UUID id);
}
