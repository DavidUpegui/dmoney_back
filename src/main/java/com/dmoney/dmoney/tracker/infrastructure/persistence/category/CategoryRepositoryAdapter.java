package com.dmoney.dmoney.tracker.infrastructure.persistence.category;

import com.dmoney.dmoney.tracker.domain.category.Category;
import com.dmoney.dmoney.tracker.domain.category.CategoryId;
import com.dmoney.dmoney.tracker.domain.category.CategoryName;
import com.dmoney.dmoney.tracker.domain.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryAdapter implements CategoryRepository {

    private final CategoryJpaRepository jpaRepo;

    @Override
    public Optional<Category> findById(CategoryId id) {
        return jpaRepo.findById(id.value())
                .map(CategoryMapper::toDomain);
    }

    @Override
    public List<Category> findAll() {
        return jpaRepo.findAll().stream()
                .map(CategoryMapper::toDomain)
                .toList();
    }

    @Override
    public Category save(Category category) {
        CategoryEntity categoryEntity = CategoryMapper.toEntity(category);
        CategoryEntity savedCategoryEntity = jpaRepo.save(categoryEntity);
        return CategoryMapper.toDomain(savedCategoryEntity);
    }

    @Override
    public boolean existsByName(CategoryName name) {
        return jpaRepo.existsByNameIgnoreCase(name.value());
    }

    @Override
    public void delete(CategoryId id) {

    }
}
