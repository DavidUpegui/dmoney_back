package com.dmoney.dmoney.tracker.domain.movement.service;

import com.dmoney.dmoney.shared.domain.exceptions.ResourceNotFoundException;
import com.dmoney.dmoney.shared.domain.models.UserId;
import com.dmoney.dmoney.tracker.domain.category.exceptions.SubcategoryDoesNotBelongToCategoryException;
import com.dmoney.dmoney.tracker.domain.category.model.Category;
import com.dmoney.dmoney.tracker.domain.category.model.CategoryId;
import com.dmoney.dmoney.tracker.domain.category.model.SubcategoryId;
import com.dmoney.dmoney.tracker.domain.category.repository.CategoryRepository;
import com.dmoney.dmoney.tracker.domain.tag.model.TagId;
import com.dmoney.dmoney.tracker.domain.tag.repository.TagRepository;

import java.util.Set;

public class MovementCategorizationValidator {

    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;

    public MovementCategorizationValidator(
            CategoryRepository categoryRepository,
            TagRepository tagRepository){
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
    }

    public void validate(UserId userId, CategoryId catId, SubcategoryId subId, Set<TagId> tagsId){
        Category category = categoryRepository.findByUserIdAndId(userId, catId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", catId.value().toString()));

        if(category.subcategories().stream().noneMatch(s -> s.id().equals(subId))){
            throw new SubcategoryDoesNotBelongToCategoryException(catId.value().toString(), subId.value().toString());
        }
        for (TagId tagId : tagsId) {
            if (!tagRepository.existsByUserIdAndId(userId,tagId)) {
                throw new ResourceNotFoundException("Tag", "Id", tagId.toString());
            }
        }
    }
}
