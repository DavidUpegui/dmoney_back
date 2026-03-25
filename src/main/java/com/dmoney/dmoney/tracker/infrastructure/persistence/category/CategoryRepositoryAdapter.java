package com.dmoney.dmoney.tracker.infrastructure.persistence.category;

import com.dmoney.dmoney.shared.domain.models.UserId;
import com.dmoney.dmoney.tracker.domain.category.model.Category;
import com.dmoney.dmoney.tracker.domain.category.model.CategoryId;
import com.dmoney.dmoney.tracker.domain.category.model.CategoryName;
import com.dmoney.dmoney.tracker.domain.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryAdapter implements CategoryRepository {

    private final CategoryJpaRepository jpaRepo;

    @Override
    public Optional<Category> findByUserIdAndId(UserId userId, CategoryId id) {
        return jpaRepo.findByUserIdAndId(userId.value(), id.value())
                .map(CategoryMapper::toDomain);
    }

    @Override
    public List<Category> findAllByUserId(UserId userId) {
        return jpaRepo.findAllByUserId(userId.value()).stream()
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
    public boolean existsByUserIdAndNameIgnoreCase(UserId userId,CategoryName name) {
        return jpaRepo.existsByUserIdAndNameIgnoreCase(userId.value(), name.value());
    }

    @Override
    public boolean deleteByUserIdAndId(UserId userId, CategoryId id) {
        int deleted = jpaRepo.deleteByUserIdAndId(userId.value(), id.value());
        return deleted > 0;
    }
}
