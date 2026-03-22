package com.dmoney.dmoney.tracker.infrastructure.persistence.category;

import com.dmoney.dmoney.tracker.domain.category.Category;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryJpaRepository extends JpaRepository<CategoryEntity, UUID> {

    @Modifying(clearAutomatically = true)
    @Transactional
    int deleteByUserIdAndId(UUID userId, UUID catId);
    boolean existsByUserIdAndNameIgnoreCase(UUID userId, String name);
    Optional<CategoryEntity> findByUserIdAndId(UUID userId, UUID categoryId);
    List<CategoryEntity> findAllByUserId(UUID userId);

}
