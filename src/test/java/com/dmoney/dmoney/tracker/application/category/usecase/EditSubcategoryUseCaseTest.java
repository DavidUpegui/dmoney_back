package com.dmoney.dmoney.tracker.application.category.usecase;

import com.dmoney.dmoney.shared.domain.models.UserId;
import com.dmoney.dmoney.tracker.application.category.commands.EditSubcategoryCommand;
import com.dmoney.dmoney.tracker.application.category.helpers.CategoryLoader;
import com.dmoney.dmoney.tracker.application.category.result.SubcategoryResult;
import com.dmoney.dmoney.shared.domain.exceptions.ResourceNotFoundException;
import com.dmoney.dmoney.tracker.application.port.AuthenticatedUserProvider;
import com.dmoney.dmoney.tracker.domain.category.model.*;
import com.dmoney.dmoney.tracker.domain.category.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EditSubcategoryUseCaseTest {
    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryLoader categoryLoader;

    @Mock
    private AuthenticatedUserProvider authProvider;

    @InjectMocks
    private EditSubcategoryUseCase editSubcategoryUseCase;

    private UserId userId;
    private Category category;
    private Subcategory subcategory;

    @BeforeEach
    void setUp(){
        userId = UserId.newId();
        category = Category.create(
                userId,
                CategoryName.from("Category name"),
                CategoryDescription.from("Category description"),
                CategoryType.INCOME
        );
        subcategory = category.addSubcategory(
                SubcategoryName.from("Subcategory name"),
                SubcategoryDescription.from("Subcategory description")
        );
        when(authProvider.currentUserId()).thenReturn(userId);
    }

    @Test
    void cat15_updates_subcategory(){
        CategoryId categoryId = category.id();
        SubcategoryId subcategoryId = subcategory.id();

        String newSubcategoryName = "New name";
        String newSubcategoryDescription = "New description";

        EditSubcategoryCommand command = new EditSubcategoryCommand(
                categoryId.value().toString(),
                subcategoryId.value().toString(),
                newSubcategoryName,
                newSubcategoryDescription,
                null,
                null
        );

        when(categoryLoader.load(userId, categoryId))
                .thenReturn(category);

        when(categoryRepository.save(any()))
                .thenAnswer( invocation ->
                        invocation.getArgument(0)
                );

        SubcategoryResult subcategoryResult =
                editSubcategoryUseCase.execute(command);

        assertEquals(newSubcategoryName, subcategoryResult.subcategoryName());
        assertEquals(newSubcategoryDescription, subcategoryResult.subcategoryDescription());

        verify(categoryLoader).load(userId, categoryId);
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void cat15_updates_only_name(){
        CategoryId categoryId = category.id();
        SubcategoryId subcategoryId = subcategory.id();

        String newSubcategoryName = "New name";
        String originalSubcategoryDescription =
                subcategory.description().value();

        EditSubcategoryCommand command = new EditSubcategoryCommand(
                categoryId.value().toString(),
                subcategoryId.value().toString(),
                newSubcategoryName,
                null,
                null,
                null
        );

        when(categoryLoader.load(userId, categoryId))
                .thenReturn(category);

        when(categoryRepository.save(any()))
                .thenAnswer( invocation ->
                        invocation.getArgument(0)
                );

        SubcategoryResult subcategoryResult =
                editSubcategoryUseCase.execute(command);

        assertEquals(newSubcategoryName, subcategoryResult.subcategoryName());
        assertEquals(originalSubcategoryDescription, subcategoryResult.subcategoryDescription());

        verify(categoryLoader).load(userId, categoryId);
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void cat15_updates_only_description(){
        CategoryId categoryId = category.id();
        SubcategoryId subcategoryId = subcategory.id();

        String originalSubcategoryName = subcategory.name().value();
        String newSubcategoryDescription = "New description";

        EditSubcategoryCommand command = new EditSubcategoryCommand(
                categoryId.value().toString(),
                subcategoryId.value().toString(),
                null,
                newSubcategoryDescription,
                null,
                null
        );

        when(categoryLoader.load(userId, categoryId))
                .thenReturn(category);

        when(categoryRepository.save(any()))
                .thenAnswer( invocation ->
                        invocation.getArgument(0)
                );

        SubcategoryResult subcategoryResult =
                editSubcategoryUseCase.execute(command);

        assertEquals(originalSubcategoryName, subcategoryResult.subcategoryName());
        assertEquals(newSubcategoryDescription, subcategoryResult.subcategoryDescription());

        verify(categoryLoader).load(userId, categoryId);
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void cat17_rejects_foreign_category_on_subcategory_edit(){
        CategoryId anyId = CategoryId.newId();
        SubcategoryId subcategoryId = subcategory.id();

        String newSubcategoryName = "New name";
        String newSubcategoryDescription = "New description";

        EditSubcategoryCommand command = new EditSubcategoryCommand(
                anyId.value().toString(),
                subcategoryId.value().toString(),
                newSubcategoryName,
                newSubcategoryDescription,
                null,
                null
        );

        when(categoryLoader.load(userId, anyId))
                .thenThrow(new ResourceNotFoundException(
                        "Category",
                        "id",
                        anyId.value().toString()
                ));

        assertThrows(ResourceNotFoundException.class,
                () -> editSubcategoryUseCase.execute(command));

        verify(categoryLoader).load(userId, anyId);
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void cat15_rejects_missing_subcategory(){
        CategoryId categoryId = category.id();
        SubcategoryId anyId = SubcategoryId.newId();

        String newSubcategoryName = "New name";
        String newSubcategoryDescription = "New description";

        EditSubcategoryCommand command = new EditSubcategoryCommand(
                categoryId.value().toString(),
                anyId.value().toString(),
                newSubcategoryName,
                newSubcategoryDescription,
                null,
                null
        );

        when(categoryLoader.load(userId, categoryId))
                .thenReturn(category);

        assertThrows(ResourceNotFoundException.class,
                () -> editSubcategoryUseCase.execute(command));

        verify(categoryLoader).load(userId, categoryId);
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void cat22_updatesSubcategoryIconAndColor() {
        CategoryId categoryId = category.id();
        SubcategoryId subcategoryId = subcategory.id();

        EditSubcategoryCommand command = new EditSubcategoryCommand(
                categoryId.value().toString(),
                subcategoryId.value().toString(),
                null,
                null,
                "carrot",
                "green"
        );

        when(categoryLoader.load(userId, categoryId)).thenReturn(category);
        when(categoryRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        SubcategoryResult result = editSubcategoryUseCase.execute(command);

        assertEquals("carrot", result.icon());
        assertEquals("green", result.color());
    }

    @Test
    void cat22_clearsSubcategoryIconToInherit() {
        CategoryId categoryId = category.id();
        SubcategoryId subcategoryId = subcategory.id();
        subcategory.changeIcon(CategoryIcon.from("carrot"));
        subcategory.changeColor(CategoryColor.from("green"));

        EditSubcategoryCommand command = new EditSubcategoryCommand(
                categoryId.value().toString(),
                subcategoryId.value().toString(),
                null,
                null,
                "",
                ""
        );

        when(categoryLoader.load(userId, categoryId)).thenReturn(category);
        when(categoryRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        SubcategoryResult result = editSubcategoryUseCase.execute(command);

        assertNull(result.icon());
        assertNull(result.color());
    }

    @Test
    void cat22_keepsSubcategoryVisualWhenAbsent() {
        CategoryId categoryId = category.id();
        SubcategoryId subcategoryId = subcategory.id();
        subcategory.changeIcon(CategoryIcon.from("carrot"));
        subcategory.changeColor(CategoryColor.from("green"));

        EditSubcategoryCommand command = new EditSubcategoryCommand(
                categoryId.value().toString(),
                subcategoryId.value().toString(),
                null,
                null,
                null,
                null
        );

        when(categoryLoader.load(userId, categoryId)).thenReturn(category);
        when(categoryRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        SubcategoryResult result = editSubcategoryUseCase.execute(command);

        assertEquals("carrot", result.icon());
        assertEquals("green", result.color());
    }

}
