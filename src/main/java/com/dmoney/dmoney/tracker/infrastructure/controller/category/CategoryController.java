package com.dmoney.dmoney.tracker.infrastructure.controller.category;

import com.dmoney.dmoney.tracker.application.category.create.CreateCategoryCommand;
import com.dmoney.dmoney.tracker.application.category.create.CreateCategoryUseCase;
import com.dmoney.dmoney.tracker.application.category.create.FindAllCategoriesUseCase;
import com.dmoney.dmoney.tracker.domain.category.Category;
import com.dmoney.dmoney.tracker.exceptions.CategoryAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CreateCategoryUseCase createCategoryUseCase;
    private final FindAllCategoriesUseCase findAllCategoriesUseCase;

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(
            @RequestBody CreateCategoryRequest request
    ){
            CreateCategoryCommand command = new CreateCategoryCommand(
                    request.name(),
                    request.description()
            );

            Category categoryCreated = createCategoryUseCase.execute(command);
            CategoryResponse response = CategoryWebMapper.toResponse(categoryCreated);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> findAllCategories(){
        return ResponseEntity
                .ok()
                .body(findAllCategoriesUseCase.execute()
                        .stream()
                        .map(CategoryWebMapper::toResponse)
                        .toList());
    }
}
