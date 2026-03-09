package com.dmoney.dmoney.tracker.domain.category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {
    Optional<Category> findById(CategoryId id);
    List<Category> findAll();
    Category save(Category category);
    boolean existsById(CategoryId id);
    boolean existsByNameIgnoreCase(CategoryName name);
    void delete(CategoryId id);
}
