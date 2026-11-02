package com.dmoney.dmoney.tracker.infrastructure.persistence.category;

import com.dmoney.dmoney.shared.domain.models.UserId;
import com.dmoney.dmoney.tracker.domain.category.model.*;

import java.util.Set;
import java.util.stream.Collectors;

public final class CategoryMapper {

    private CategoryMapper(){}

    public static CategoryEntity toEntity(Category category){
        CategoryEntity entity = new CategoryEntity(
                category.id().value(),
                category.userId().value(),
                category.name().value(),
                category.description().value(),
                category.categoryType().toString(),
                category.icon() == null ? null : category.icon().value(),
                category.color() == null ? null : category.color().value()
        );

        category.subcategories().forEach(sub -> {
            SubcategoryEntity subcategoryEntity = new SubcategoryEntity(
                    sub.id().value(),
                    sub.name().value(),
                    sub.description().value(),
                    entity,
                    sub.icon() == null ? null : sub.icon().value(),
                    sub.color() == null ? null : sub.color().value()
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
                        new SubcategoryDescription(subEntity.getDescription()),
                        CategoryIcon.fromNullable(subEntity.getIcon()),
                        CategoryColor.fromNullable(subEntity.getColor())
                ))
                .collect(Collectors.toSet());

        return Category.rehydrate(
                UserId.fromUUID(entity.getUserId()),
                CategoryId.from(entity.getId().toString()),
                CategoryName.from(entity.getName()),
                CategoryDescription.from(entity.getDescription()),
                subcategorySet,
                CategoryType.valueOf(entity.getType()),
                CategoryIcon.fromNullable(entity.getIcon()),
                CategoryColor.fromNullable(entity.getColor())
        );
    }
}
