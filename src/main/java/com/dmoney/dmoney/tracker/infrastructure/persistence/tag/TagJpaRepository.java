package com.dmoney.dmoney.tracker.infrastructure.persistence.tag;

import com.dmoney.dmoney.tracker.domain.tag.Tag;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TagJpaRepository extends JpaRepository<TagEntity, UUID> {

    @Modifying(clearAutomatically = true)
    @Transactional
    int deleteByUserIdAndId(UUID userId, UUID id);

    boolean existsByUserIdAndNameIgnoreCase(UUID userId, String name);
    List<TagEntity> findAllByUserId(UUID userId);
    Optional<TagEntity> findByUserIdAndId(UUID userId, UUID id);
}
