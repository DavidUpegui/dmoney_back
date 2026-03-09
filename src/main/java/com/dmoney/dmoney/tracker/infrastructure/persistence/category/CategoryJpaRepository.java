package com.dmoney.dmoney.tracker.infrastructure.persistence.category;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CategoryJpaRepository extends JpaRepository<CategoryEntity, UUID> {
    boolean existsByNameIgnoreCase(String name);
}
