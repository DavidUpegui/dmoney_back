package com.dmoney.dmoney.tracker.application.category;

import com.dmoney.dmoney.shared.domain.models.UserId;
import com.dmoney.dmoney.tracker.application.port.AuthenticatedUserProvider;
import com.dmoney.dmoney.tracker.domain.category.CategoryId;
import com.dmoney.dmoney.tracker.domain.category.CategoryRepository;
import com.dmoney.dmoney.shared.domain.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteCategoryUseCase {

    private final CategoryRepository categoryRepository;
    private final AuthenticatedUserProvider authProvider;

    public void execute(String categoryId){
        UserId userId = authProvider.currentUserId();
        CategoryId catId = CategoryId.from(categoryId);

        boolean deleted = categoryRepository.deleteByUserIdAndId(userId, catId);
        if(!deleted){
            throw new ResourceNotFoundException("Tag", "id", categoryId);
        }
    }
}
