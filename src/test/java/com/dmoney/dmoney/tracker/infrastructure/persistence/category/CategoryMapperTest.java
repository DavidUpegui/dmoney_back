package com.dmoney.dmoney.tracker.infrastructure.persistence.category;

import com.dmoney.dmoney.tracker.domain.category.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class CategoryMapperTest {

    @Test
    void should_map_category_to_category_entity(){
        Category categoryDomain = new Category(
                CategoryId.newId(),
                CategoryName.from("Category name"),
                Description.from("Category Description")
        );

        categoryDomain.addSubcategory(
                SubcategoryName.from("Subcategory1 name"),
                Description.from("Subcategory1 description")
        );

        categoryDomain.addSubcategory(
                SubcategoryName.from("Subcategory2 name"),
                Description.from("Subcategory2 description")
        );

        CategoryEntity categoryEntity = CategoryMapper.toEntity(categoryDomain);

        assertEquals(categoryDomain.name().value(), categoryEntity.getName());
        assertEquals(categoryDomain.id().value(), categoryEntity.getId());
        assertEquals(categoryDomain.description().value(), categoryEntity.getDescription());

        Assertions.assertThat(categoryEntity.getSubcategories())
                .hasSize(2)
                .extracting(SubcategoryEntity::getName)
                .containsExactlyInAnyOrder(
                        "Subcategory1 name",
                        "Subcategory2 name"
                );
    }

    @Test
    void should_map_category_to_category_entity_with_sibcategories_empty(){
        Category categoryDomain = new Category(
                CategoryId.newId(),
                CategoryName.from("Category name"),
                Description.from("Category Description")
        );


        CategoryEntity categoryEntity = CategoryMapper.toEntity(categoryDomain);

        assertEquals(categoryDomain.name().value(), categoryEntity.getName());
        assertEquals(categoryDomain.id().value(), categoryEntity.getId());
        assertTrue(categoryEntity.getSubcategories().isEmpty());
    }

    @Test
    void should_map_category_entity_to_category(){
        UUID categoryId = UUID.randomUUID();
        UUID subId1 = UUID.randomUUID();
        UUID subId2 = UUID.randomUUID();

        CategoryEntity categoryEntity = getCategoryEntity(categoryId, subId1, subId2);

        Category result = CategoryMapper.toDomain(categoryEntity);

        assertEquals(categoryId, result.id().value());
        assertEquals("Tech", result.name().value());
        assertEquals("Tech description", result.description().value());

        assertEquals(2, result.subcategories().size());


        Set<UUID> mappedIds = result.subcategories()
                .stream()
                .map(sub -> sub.id().value())
                .collect(Collectors.toSet());

        assertTrue(mappedIds.contains(subId1));
        assertTrue(mappedIds.contains(subId2));

    }

    @Test
    void should_map_category_entity_to_category_with_empty_subcategories(){
        UUID categoryId = UUID.randomUUID();


        CategoryEntity categoryEntity = new CategoryEntity(
                categoryId,
                "Tech",
                "Tech description"
        );

        Category result = CategoryMapper.toDomain(categoryEntity);

        assertEquals(categoryId, result.id().value());
        assertEquals("Tech", result.name().value());
        assertEquals("Tech description", result.description().value());

        assertTrue(result.subcategories().isEmpty());





    }

    private static CategoryEntity getCategoryEntity(UUID categoryId, UUID subId1, UUID subId2) {
        CategoryEntity categoryEntity = new CategoryEntity(
                categoryId,
                "Tech",
                "Tech description"
        );

        SubcategoryEntity sub1 = new SubcategoryEntity(
                subId1,
                "Phones",
                "Phones desc",
                categoryEntity
        );

        SubcategoryEntity sub2 = new SubcategoryEntity(
                subId2,
                "Laptops",
                "Laptops desc",
                categoryEntity
        );

        Set<SubcategoryEntity> subcategories = new HashSet<>();
        subcategories.add(sub1);
        subcategories.add(sub2);

        categoryEntity.setSubcategories(subcategories);
        return categoryEntity;
    }
}
