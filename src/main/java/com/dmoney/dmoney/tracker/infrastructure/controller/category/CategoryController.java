package com.dmoney.dmoney.tracker.infrastructure.controller.category;

import com.dmoney.dmoney.tracker.application.category.*;
import com.dmoney.dmoney.tracker.application.category.commands.*;
import com.dmoney.dmoney.tracker.domain.category.*;
import com.dmoney.dmoney.tracker.infrastructure.controller.category.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
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
    private final EditSubcategoryUseCase editSubcategoryUseCase;
    private final FindAllSubcategoriesByCategoryIdUseCase findAllSubcategoriesByCategoryIdUseCase;
    private final DeleteSubcategoryUseCase deleteSubcategoryUseCase;

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

    @PatchMapping("/{categoryId}/subcategories/{subcategoryId}")
    public ResponseEntity<SubcategoryResponse> editSubcategory(
            @PathVariable String categoryId,
            @PathVariable String subcategoryId,
            @RequestBody EditSubcategoryRequest request
    ){
        SubcategoryName subcategoryName = null;

        CategoryId catId = new CategoryId(UUID.fromString(categoryId));
        SubcategoryId subcatId = new SubcategoryId(UUID.fromString(subcategoryId));

        if(request.name() != null){
            subcategoryName =  new SubcategoryName(request.name());
        }

        EditSubcategoryCommand command = new EditSubcategoryCommand(
            catId,
            subcatId,
            subcategoryName,
            request.description()
        );

        Subcategory subcategory =  editSubcategoryUseCase.execute(command);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(SubcategoryResponse.from(subcategory, catId));
    }

    @GetMapping("/{categoryId}/subcategories")
    public ResponseEntity<List<SubcategoryResponse>> findAllSubcategoriesByCatId(
            @PathVariable String categoryId
    ){
        CategoryId catId = new CategoryId(UUID.fromString(categoryId));

        Set<Subcategory> subcategories =
                findAllSubcategoriesByCategoryIdUseCase.execute(catId);

        return ResponseEntity
                .ok()
                .body(subcategories.stream()
                        .map((sc) ->SubcategoryResponse.from(sc, catId))
                        .toList());
    }

    @DeleteMapping("/{pathCategoryId}/subcategories/{pathSubcategoryId}")
    public ResponseEntity<Void> deleteSubcategory(
            @PathVariable String pathCategoryId,
            @PathVariable String pathSubcategoryId
    ){
        CategoryId categoryId = new CategoryId(UUID.fromString(pathCategoryId));
        SubcategoryId subcategoryId = new SubcategoryId(UUID.fromString(pathSubcategoryId));

        deleteSubcategoryUseCase.execute(new DeleteSubcategoryCommand(
                categoryId,
                subcategoryId
        ));

        return ResponseEntity.noContent().build();
    }
}
