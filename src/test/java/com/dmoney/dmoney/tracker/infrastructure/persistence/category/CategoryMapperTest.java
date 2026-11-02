package com.dmoney.dmoney.tracker.infrastructure.persistence.category;

import com.dmoney.dmoney.shared.domain.models.UserId;
import com.dmoney.dmoney.tracker.domain.category.model.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class CategoryMapperTest {

    @Test
    void should_map_category_to_category_entity(){
        UserId userId = UserId.newId();
        Category categoryDomain = Category.create(
                userId,
                CategoryName.from("Category name"),
                CategoryDescription.from("Category Description"),
                CategoryType.OUTCOME
        );

        categoryDomain.addSubcategory(
                SubcategoryName.from("Subcategory1 name"),
                SubcategoryDescription.from("Subcategory1 description")
        );

        categoryDomain.addSubcategory(
                SubcategoryName.from("Subcategory2 name"),
                SubcategoryDescription.from("Subcategory2 description")
        );

        CategoryEntity categoryEntity = CategoryMapper.toEntity(categoryDomain);

        assertEquals(categoryDomain.userId().value(), categoryEntity.getUserId());
        assertEquals(categoryDomain.name().value(), categoryEntity.getName());
        assertEquals(categoryDomain.id().value(), categoryEntity.getId());
        assertEquals(categoryDomain.description().value(), categoryEntity.getDescription());
        assertEquals(categoryDomain.categoryType().toString(), categoryEntity.getType());

        Assertions.assertThat(categoryEntity.getSubcategories())
                .hasSize(2)
                .extracting(SubcategoryEntity::getName)
                .containsExactlyInAnyOrder(
                        "Subcategory1 name",
                        "Subcategory2 name"
                );
    }

    @Test
    void should_map_category_to_category_entity_with_subcategories_empty(){
        UserId userId = UserId.newId();
        Category categoryDomain = Category.create(
                userId,
                CategoryName.from("Category name"),
                CategoryDescription.from("Category Description"),
                CategoryType.OUTCOME
        );


        CategoryEntity categoryEntity = CategoryMapper.toEntity(categoryDomain);

        assertEquals(categoryDomain.userId().value(), categoryEntity.getUserId());
        assertEquals(categoryDomain.name().value(), categoryEntity.getName());
        assertEquals(categoryDomain.id().value(), categoryEntity.getId());
        assertTrue(categoryEntity.getSubcategories().isEmpty());
        assertEquals(categoryDomain.categoryType().toString(), categoryEntity.getType());
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
        UUID userId = UUID.randomUUID();
        CategoryEntity categoryEntity = new CategoryEntity(
                categoryId,
                userId,
                "Tech",
                "Tech description",
                "INCOME",
                null,
                null
        );

        Category result = CategoryMapper.toDomain(categoryEntity);

        assertEquals(categoryId, result.id().value());
        assertEquals("Tech", result.name().value());
        assertEquals("Tech description", result.description().value());

        assertTrue(result.subcategories().isEmpty());
    }

    private static CategoryEntity getCategoryEntity(UUID categoryId, UUID subId1, UUID subId2) {
        UUID userId = UUID.randomUUID();
        CategoryEntity categoryEntity = new CategoryEntity(
                categoryId,
                userId,
                "Tech",
                "Tech description",
                "OUTCOME",
                null,
                null
        );

        SubcategoryEntity sub1 = new SubcategoryEntity(
                subId1,
                "Phones",
                "Phones desc",
                categoryEntity,
                null,
                null
        );

        SubcategoryEntity sub2 = new SubcategoryEntity(
                subId2,
                "Laptops",
                "Laptops desc",
                categoryEntity,
                null,
                null
        );

        Set<SubcategoryEntity> subcategories = new HashSet<>();
        subcategories.add(sub1);
        subcategories.add(sub2);

        categoryEntity.setSubcategories(subcategories);
        return categoryEntity;
    }

    @Test
    void shouldHavePrivateConstructor() throws Exception {
        Constructor<CategoryMapper> constructor =
                CategoryMapper.class.getDeclaredConstructor();

        assertTrue(java.lang.reflect.Modifier.isPrivate(constructor.getModifiers()));

        constructor.setAccessible(true);
        constructor.newInstance();
    }

    @Test
    void cat18_mapsCategoryVisualToEntity() {
        Category category = Category.create(
                UserId.newId(),
                CategoryName.from("Food"),
                CategoryDescription.from("Food"),
                CategoryType.OUTCOME,
                CategoryIcon.from("utensils"),
                CategoryColor.from("amber")
        );

        CategoryEntity entity = CategoryMapper.toEntity(category);

        assertEquals("utensils", entity.getIcon());
        assertEquals("amber", entity.getColor());
    }

    @Test
    void cat22_mapsSubcategoryVisualToEntity() {
        Category category = Category.create(
                UserId.newId(),
                CategoryName.from("Food"),
                CategoryDescription.from("Food"),
                CategoryType.OUTCOME
        );
        category.addSubcategory(
                SubcategoryName.from("Groceries"),
                SubcategoryDescription.from("Groceries"),
                CategoryIcon.from("shopping-basket"),
                CategoryColor.from("green")
        );

        CategoryEntity entity = CategoryMapper.toEntity(category);
        SubcategoryEntity subcategoryEntity = entity.getSubcategories().iterator().next();

        assertEquals("shopping-basket", subcategoryEntity.getIcon());
        assertEquals("green", subcategoryEntity.getColor());
    }

    @Test
    void cat23_mapsNullVisualsBackToDomain() {
        UUID categoryId = UUID.randomUUID();
        UUID subcategoryId = UUID.randomUUID();
        CategoryEntity entity = new CategoryEntity(
                categoryId,
                UUID.randomUUID(),
                "Food",
                "Food",
                "OUTCOME",
                null,
                null
        );
        entity.getSubcategories().add(new SubcategoryEntity(
                subcategoryId,
                "Groceries",
                "Groceries",
                entity,
                null,
                null
        ));

        Category domain = CategoryMapper.toDomain(entity);

        assertNull(domain.icon());
        assertNull(domain.color());
        Subcategory subcategory = domain.subcategories().iterator().next();
        assertNull(subcategory.icon());
        assertNull(subcategory.color());
    }

}
