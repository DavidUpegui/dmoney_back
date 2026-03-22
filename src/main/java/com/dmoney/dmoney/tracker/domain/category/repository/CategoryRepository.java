package com.dmoney.dmoney.tracker.domain.category.repository;

import com.dmoney.dmoney.shared.domain.models.UserId;
import com.dmoney.dmoney.tracker.domain.category.model.Category;
import com.dmoney.dmoney.tracker.domain.category.model.CategoryId;
import com.dmoney.dmoney.tracker.domain.category.model.CategoryName;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {
    Optional<Category> findByUserIdAndId(UserId userId, CategoryId id);
    List<Category> findAllByUserId(UserId userId);
    Category save(Category category);
    boolean existsByUserIdAndNameIgnoreCase(UserId userId, CategoryName name);
    boolean deleteByUserIdAndId(UserId userId, CategoryId id);
}
