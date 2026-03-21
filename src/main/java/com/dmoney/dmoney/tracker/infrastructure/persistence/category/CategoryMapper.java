package com.dmoney.dmoney.tracker.infrastructure.persistence.category;

import com.dmoney.dmoney.tracker.domain.category.*;

import java.util.Set;
import java.util.stream.Collectors;

public final class CategoryMapper {

    private CategoryMapper(){}

    public static CategoryEntity toEntity(Category category){
        CategoryEntity entity = new CategoryEntity(
                category.id().value(),
                category.name().value(),
                category.description().value()
        );

        category.subcategories().forEach(sub -> {
            SubcategoryEntity subcategoryEntity = new SubcategoryEntity(
                    sub.id().value(),
                    sub.name().value(),
                    sub.description().value(),
                    entity
            );
            entity.getSubcategories().add(subcategoryEntity);
        });

        return entity;
    }

    public static Category toDomain(CategoryEntity entity){
        Set<Subcategory> subcategorySet = entity.getSubcategories().stream()
                .map(subEntity -> new Subcategory(
                        new SubcategoryId(subEntity.getId()),
                        new SubcategoryName(subEntity.getName()),
                        new SubcategoryDescription(subEntity.getDescription())
                ))
                .collect(Collectors.toSet());

        return new Category(
                new CategoryId(entity.getId()),
                new CategoryName(entity.getName()),
                new CategoryDescription(entity.getDescription()),
                subcategorySet
        );
    }
}
