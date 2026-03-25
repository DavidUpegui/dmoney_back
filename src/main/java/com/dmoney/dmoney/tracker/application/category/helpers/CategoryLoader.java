package com.dmoney.dmoney.tracker.application.category.helpers;

import com.dmoney.dmoney.shared.domain.models.UserId;

import com.dmoney.dmoney.tracker.domain.category.model.Category;
import com.dmoney.dmoney.tracker.domain.category.model.CategoryId;
import com.dmoney.dmoney.tracker.domain.category.repository.CategoryRepository;
import com.dmoney.dmoney.shared.domain.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryLoader {

    private final CategoryRepository categoryRepository;

    public  Category load(UserId userId, CategoryId id){
        return categoryRepository.findByUserIdAndId(userId, id)
                .orElseThrow(()-> new ResourceNotFoundException(
                        "Category",
                        "id",
                        id.value().toString()
                ));
    }
}
