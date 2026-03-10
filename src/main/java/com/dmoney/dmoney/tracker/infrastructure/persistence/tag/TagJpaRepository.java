package com.dmoney.dmoney.tracker.infrastructure.persistence.tag;

import com.dmoney.dmoney.tracker.domain.tag.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TagJpaRepository extends JpaRepository<TagEntity, UUID> {

    boolean existsByNameIgnoreCase(String name);
}
