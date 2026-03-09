package com.dmoney.dmoney.tracker.application.category;

import com.dmoney.dmoney.tracker.domain.category.CategoryId;
import com.dmoney.dmoney.tracker.domain.category.CategoryRepository;
import com.dmoney.dmoney.tracker.domain.exceptions.ResourceAlreadyExistsException;
import com.dmoney.dmoney.tracker.domain.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteCategoryUseCase {

    private final CategoryRepository categoryRepository;

    public void execute(String categoryId){
        CategoryId catId = CategoryId.from(categoryId);
        if(!categoryRepository.existsById(catId)){
            throw new ResourceNotFoundException("Category", "id", catId.value().toString());
        }

        categoryRepository.delete(catId);
    }
}
