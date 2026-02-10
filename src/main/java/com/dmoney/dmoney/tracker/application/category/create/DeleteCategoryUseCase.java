package com.dmoney.dmoney.tracker.application.category.create;

import com.dmoney.dmoney.tracker.domain.category.Category;
import com.dmoney.dmoney.tracker.domain.category.CategoryId;
import com.dmoney.dmoney.tracker.domain.category.CategoryRepository;
import com.dmoney.dmoney.tracker.exceptions.CategoryNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteCategoryUseCase {

    private final CategoryRepository categoryRepository;

    public void execute(CategoryId categoryId){
        if(!categoryRepository.existsById(categoryId)){
            throw new CategoryNotFoundException("id", categoryId.value().toString());
        }

        categoryRepository.delete(categoryId);
    }

}
