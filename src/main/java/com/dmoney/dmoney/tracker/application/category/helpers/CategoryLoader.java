package com.dmoney.dmoney.tracker.application.category.helpers;

import com.dmoney.dmoney.shared.domain.models.UserId;
import com.dmoney.dmoney.tracker.application.port.AuthenticatedUserProvider;
import com.dmoney.dmoney.tracker.domain.category.Category;
import com.dmoney.dmoney.tracker.domain.category.CategoryId;
import com.dmoney.dmoney.tracker.domain.category.CategoryRepository;
import com.dmoney.dmoney.shared.domain.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryLoader {

    private final CategoryRepository categoryRepository;
    private final AuthenticatedUserProvider authProvider;

    public  Category load(CategoryId id){
        UserId userId = authProvider.currentUserId();
        return categoryRepository.findByUserIdAndId(userId, id)
                .orElseThrow(()-> new ResourceNotFoundException(
                        "Category",
                        "id",
                        id.value().toString()
                ));
    }
}
