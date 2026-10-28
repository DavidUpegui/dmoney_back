package com.dmoney.dmoney.tracker.domain.movement.service;

import com.dmoney.dmoney.shared.domain.exceptions.ResourceNotFoundException;
import com.dmoney.dmoney.shared.domain.models.UserId;
import com.dmoney.dmoney.tracker.domain.category.exceptions.SubcategoryDoesNotBelongToCategoryException;
import com.dmoney.dmoney.tracker.domain.category.model.Category;
import com.dmoney.dmoney.tracker.domain.category.model.CategoryDescription;
import com.dmoney.dmoney.tracker.domain.category.model.CategoryId;
import com.dmoney.dmoney.tracker.domain.category.model.CategoryName;
import com.dmoney.dmoney.tracker.domain.category.model.CategoryType;
import com.dmoney.dmoney.tracker.domain.category.model.Subcategory;
import com.dmoney.dmoney.tracker.domain.category.model.SubcategoryDescription;
import com.dmoney.dmoney.tracker.domain.category.model.SubcategoryId;
import com.dmoney.dmoney.tracker.domain.category.model.SubcategoryName;
import com.dmoney.dmoney.tracker.domain.category.repository.CategoryRepository;
import com.dmoney.dmoney.tracker.domain.tag.model.TagId;
import com.dmoney.dmoney.tracker.domain.tag.repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovementCategorizationValidatorTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private TagRepository tagRepository;

    private MovementCategorizationValidator validator;

    private UserId userId;
    private Category category;
    private Subcategory subcategory;
    private TagId tagId;

    @BeforeEach
    void setUp(){
        validator = new MovementCategorizationValidator(categoryRepository, tagRepository);
        userId = UserId.newId();
        category = Category.create(
                userId,
                CategoryName.from("Food"),
                CategoryDescription.from("Food category"),
                CategoryType.OUTCOME
        );
        subcategory = category.addSubcategory(
                SubcategoryName.from("Groceries"),
                SubcategoryDescription.from("Groceries subcategory")
        );
        tagId = TagId.newId();
    }

    @Test
    void shouldPassWhenCategorySubcategoryAndTagsAreValid(){
        when(categoryRepository.findByUserIdAndId(userId, category.id()))
                .thenReturn(Optional.of(category));
        when(tagRepository.existsByUserIdAndId(userId, tagId))
                .thenReturn(true);

        assertDoesNotThrow(() ->
                validator.validate(userId, category.id(), subcategory.id(), Set.of(tagId)));
    }

    @Test
    void shouldPassWhenTagsAreEmpty(){
        when(categoryRepository.findByUserIdAndId(userId, category.id()))
                .thenReturn(Optional.of(category));

        assertDoesNotThrow(() ->
                validator.validate(userId, category.id(), subcategory.id(), Set.of()));
    }

    @Test
    void shouldThrowWhenCategoryNotFound(){
        CategoryId unknownCategoryId = CategoryId.newId();
        when(categoryRepository.findByUserIdAndId(userId, unknownCategoryId))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                validator.validate(userId, unknownCategoryId, subcategory.id(), Set.of()));
    }

    @Test
    void shouldThrowWhenSubcategoryDoesNotBelongToCategory(){
        SubcategoryId foreignSubcategoryId = SubcategoryId.newId();
        when(categoryRepository.findByUserIdAndId(userId, category.id()))
                .thenReturn(Optional.of(category));

        assertThrows(SubcategoryDoesNotBelongToCategoryException.class, () ->
                validator.validate(userId, category.id(), foreignSubcategoryId, Set.of()));
    }

    @Test
    void shouldThrowWhenTagNotFound(){
        when(categoryRepository.findByUserIdAndId(userId, category.id()))
                .thenReturn(Optional.of(category));
        when(tagRepository.existsByUserIdAndId(userId, tagId))
                .thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () ->
                validator.validate(userId, category.id(), subcategory.id(), Set.of(tagId)));

        verify(tagRepository).existsByUserIdAndId(userId, tagId);
    }
}
