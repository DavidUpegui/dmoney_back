package com.dmoney.dmoney.tracker.infrastructure.controller.category;

import com.dmoney.dmoney.tracker.application.category.*;
import com.dmoney.dmoney.tracker.application.category.commands.AddSubcategoryCommand;
import com.dmoney.dmoney.tracker.application.category.commands.CreateCategoryCommand;
import com.dmoney.dmoney.tracker.application.category.commands.EditCategoryCommand;
import com.dmoney.dmoney.tracker.domain.category.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CreateCategoryUseCase createCategoryUseCase;
    private final FindAllCategoriesUseCase findAllCategoriesUseCase;
    private final FindCategoryByIdUseCase findCategoryByIdUseCase;
    private final DeleteCategoryUseCase deleteCategoryUseCase;
    private final AddSubcategoryUseCase addSubcategoryUseCase;
    private final EditCategoryUseCase editCategoryUseCase;

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

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> findCategoryById(@PathVariable String id){
        CategoryId categoryId = new CategoryId(UUID.fromString(id));

        return ResponseEntity
                .ok()
                .body(CategoryWebMapper.toResponse(
                        findCategoryByIdUseCase.execute(categoryId)
                ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable String id){
        CategoryId categoryId = new CategoryId(UUID.fromString(id));
        deleteCategoryUseCase.execute(categoryId);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{categoryId}/subcategories")
    public ResponseEntity<SubcategoryResponse> createSubcategory(
            @PathVariable String categoryId,
            @RequestBody CreateSubcategoryRequest request
    ){
        CategoryId catId = new CategoryId(UUID.fromString(categoryId));
        AddSubcategoryCommand command = new AddSubcategoryCommand(
                catId,
                new SubcategoryName(request.name()),
                request.description()
        );

        Subcategory subcategory = addSubcategoryUseCase.execute(command);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(SubcategoryResponse.from(subcategory, catId ));
    }

    @PatchMapping("/{catIdParam}")
    public ResponseEntity<CategoryResponse> editCategory(
            @PathVariable String catIdParam,
            @RequestBody EditCategoryRequest request
    ){
        CategoryId categoryId = new CategoryId(UUID.fromString(catIdParam));
        CategoryName categoryName = null;
        if(request.name() != null){
            categoryName = new CategoryName(request.name());
        }
        String description = request.description();

        EditCategoryCommand command =
                new EditCategoryCommand(categoryId, categoryName, description);

        Category category = editCategoryUseCase.execute(command);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(CategoryWebMapper.toResponse(category));

    }
}
