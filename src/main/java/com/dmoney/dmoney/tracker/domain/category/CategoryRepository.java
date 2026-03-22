package com.dmoney.dmoney.tracker.domain.category;

import com.dmoney.dmoney.shared.domain.models.UserId;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {
    Optional<Category> findByUserIdAndId(UserId userId,CategoryId id);
    List<Category> findAllByUserId(UserId userId);
    Category save(Category category);
    boolean existsByUserIdAndNameIgnoreCase(UserId userId, CategoryName name);
    boolean deleteByUserIdAndId(UserId userId, CategoryId id);
}
