package com.dmoney.dmoney.tracker.infrastructure.controller.category;

import com.dmoney.dmoney.tracker.application.category.commands.*;
import com.dmoney.dmoney.tracker.application.category.result.CategoryResult;
import com.dmoney.dmoney.tracker.application.category.result.SubcategoryResult;
import com.dmoney.dmoney.tracker.application.category.usecase.*;
import com.dmoney.dmoney.tracker.infrastructure.controller.category.dto.*;
import jakarta.validation.Valid;
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
    private final FindCategoryByIdUseCase findCategoryByIdUseCase;
    private final DeleteCategoryUseCase deleteCategoryUseCase;
    private final AddSubcategoryUseCase addSubcategoryUseCase;
    private final EditCategoryUseCase editCategoryUseCase;
    private final EditSubcategoryUseCase editSubcategoryUseCase;
    private final FindAllSubcategoriesByCategoryIdUseCase findAllSubcategoriesByCategoryIdUseCase;
    private final DeleteSubcategoryUseCase deleteSubcategoryUseCase;

    @PostMapping
    public ResponseEntity<CategoryResult> createCategory(
            @RequestBody @Valid CreateCategoryRequest request
    ){
            CreateCategoryCommand command = new CreateCategoryCommand(
                    request.name(),
                    request.description()
            );

            CategoryResult categoryCreated = createCategoryUseCase.execute(command);
            return ResponseEntity.status(HttpStatus.CREATED).body(categoryCreated);
    }

    @GetMapping
    public ResponseEntity<List<CategoryResult>> findAllCategories(){
        return ResponseEntity
                .ok()
                .body(findAllCategoriesUseCase.execute());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResult> findCategoryById(@PathVariable String id){

        return ResponseEntity
                .ok()
                .body(
                        findCategoryByIdUseCase.execute(id)
                );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable String id){
        deleteCategoryUseCase.execute(id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{categoryId}/subcategories")
    public ResponseEntity<SubcategoryResult> createSubcategory(
            @PathVariable String categoryId,
            @RequestBody @Valid CreateSubcategoryRequest request
    ){
        AddSubcategoryCommand command = new AddSubcategoryCommand(
                categoryId,
                request.name(),
                request.description()
        );

        SubcategoryResult subcategory = addSubcategoryUseCase.execute(command);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(subcategory);
    }

    @PatchMapping("/{catIdParam}")
    public ResponseEntity<CategoryResult> editCategory(
            @PathVariable String catIdParam,
            @RequestBody @Valid EditCategoryRequest request
    ){

        EditCategoryCommand command =
                new EditCategoryCommand(catIdParam, request.name(), request.description());

        CategoryResult category = editCategoryUseCase.execute(command);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(category);

    }

    @PatchMapping("/{categoryId}/subcategories/{subcategoryId}")
    public ResponseEntity<SubcategoryResult> editSubcategory(
            @PathVariable String categoryId,
            @PathVariable String subcategoryId,
            @RequestBody @Valid EditSubcategoryRequest request
    ){

        EditSubcategoryCommand command = new EditSubcategoryCommand(
            categoryId,
            subcategoryId,
                request.name(),
                request.description()
        );

        SubcategoryResult subcategory =  editSubcategoryUseCase.execute(command);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(subcategory);
    }

    @GetMapping("/{categoryId}/subcategories")
    public ResponseEntity<List<SubcategoryResult>> findAllSubcategoriesByCatId(
            @PathVariable String categoryId
    ){

        List<SubcategoryResult> subcategories =
                findAllSubcategoriesByCategoryIdUseCase.execute(categoryId);

        return ResponseEntity
                .ok()
                .body(subcategories);
    }

    @DeleteMapping("/{pathCategoryId}/subcategories/{pathSubcategoryId}")
    public ResponseEntity<Void> deleteSubcategory(
            @PathVariable String pathCategoryId,
            @PathVariable String pathSubcategoryId
    ){
        deleteSubcategoryUseCase.execute(new DeleteSubcategoryCommand(
                pathCategoryId,
                pathSubcategoryId
        ));

        return ResponseEntity.noContent().build();
    }
}
