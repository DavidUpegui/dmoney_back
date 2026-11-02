package com.dmoney.dmoney.tracker.application.category.usecase;


import com.dmoney.dmoney.shared.domain.exceptions.ResourceAlreadyExistsException;
import com.dmoney.dmoney.shared.domain.models.UserId;
import com.dmoney.dmoney.tracker.application.category.commands.EditCategoryCommand;
import com.dmoney.dmoney.tracker.application.category.helpers.CategoryLoader;
import com.dmoney.dmoney.tracker.application.category.result.CategoryResult;
import com.dmoney.dmoney.shared.domain.exceptions.ResourceNotFoundException;
import com.dmoney.dmoney.tracker.application.port.AuthenticatedUserProvider;
import com.dmoney.dmoney.tracker.domain.category.model.*;
import com.dmoney.dmoney.tracker.domain.category.repository.CategoryRepository;
import com.dmoney.dmoney.tracker.domain.category.service.CategoryUniquenessChecker;
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
class EditCategoryUseCaseTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryLoader categoryLoader;

    @Mock
    private AuthenticatedUserProvider authProvider;

    @Mock
    private CategoryUniquenessChecker uniquenessChecker;

    @InjectMocks
    EditCategoryUseCase useCase;

    UserId userId;
    Category category;

    @BeforeEach
    void setUp(){
        userId = UserId.newId();
        category = Category.create(
                userId,
                CategoryName.from("Category name"),
                CategoryDescription.from("Category description"),
                CategoryType.INCOME
        );
        when(authProvider.currentUserId()).thenReturn(userId);
    }

    @Test
    void cat08_updates_category(){
        CategoryId categoryId = category.id();
        String newName ="New Category name";
        String newDescription = "New Category description";
        String newType = "OUTCOME";

        EditCategoryCommand command = new EditCategoryCommand(
                categoryId.value().toString(),
                newName,
                newDescription,
                newType,
                null,
                null
        );


        when(categoryLoader.load(userId, categoryId))
                .thenReturn(category);

        when(categoryRepository.save(any()))
                .thenAnswer(invocation ->
                        invocation.getArgument(0)
                );

        CategoryResult editedCategory = useCase.execute(command);


        verify(categoryLoader).load(userId, categoryId);
        verify(categoryRepository).save(any(Category.class));
        assertEquals(newName, editedCategory.name());
        assertEquals(newDescription, editedCategory.description());
        assertEquals(newType, editedCategory.type());
        assertEquals(categoryId.value().toString(), editedCategory.id());
    }

    @Test
    void cat08_updates_only_name(){
        CategoryId categoryId = category.id();
        String newName ="New Category name";

        EditCategoryCommand command = new EditCategoryCommand(
                categoryId.value().toString(),
                newName,
                null,
                null,
                null,
                null
        );

        when(categoryLoader.load(userId,categoryId))
                .thenReturn(category);

        when(categoryRepository.save(any()))
                .thenAnswer(invocation ->
                        invocation.getArgument(0)
                );

        CategoryResult editedCategory = useCase.execute(command);


        verify(categoryLoader).load(userId, categoryId);
        verify(categoryRepository).save(any(Category.class));

        assertEquals(newName, editedCategory.name());
        assertEquals(
                category.description().value(),
                editedCategory.description()
        );
        assertEquals(category.categoryType().toString(), editedCategory.type());
        assertEquals(categoryId.value().toString(), editedCategory.id());
    }

    @Test
    void cat08_updates_only_description(){
        CategoryId categoryId = category.id();
        String newDescription = "New Category description";


        EditCategoryCommand command = new EditCategoryCommand(
                categoryId.value().toString(),
                null,
                newDescription,
                null,
                null,
                null
        );

        when(categoryLoader.load(userId, categoryId))
                .thenReturn(category);

        when(categoryRepository.save(any()))
                .thenAnswer(invocation ->
                        invocation.getArgument(0)
                );

        CategoryResult editedCategory = useCase.execute(command);


        verify(categoryLoader).load(userId, categoryId);
        verify(categoryRepository).save(any(Category.class));

        assertEquals(category.name().value(), editedCategory.name());
        assertEquals(newDescription, editedCategory.description());
        assertEquals(category.categoryType().toString(), editedCategory.type());
        assertEquals(categoryId.value().toString(), editedCategory.id());
    }

    @Test
    void cat08_updates_only_type(){
        CategoryId categoryId = category.id();
        String newType = "OUTCOME";


        EditCategoryCommand command = new EditCategoryCommand(
                categoryId.value().toString(),
                null,
                null,
                newType,
                null,
                null
        );

        when(categoryLoader.load(userId, categoryId))
                .thenReturn(category);

        when(categoryRepository.save(any()))
                .thenAnswer(invocation ->
                        invocation.getArgument(0)
                );

        CategoryResult editedCategory = useCase.execute(command);


        verify(categoryLoader).load(userId, categoryId);
        verify(categoryRepository).save(any(Category.class));

        assertEquals(category.name().value(), editedCategory.name());
        assertEquals(category.description().value(), editedCategory.description());
        assertEquals(newType, editedCategory.type());
        assertEquals(categoryId.value().toString(), editedCategory.id());
    }

    @Test
    void cat05_rejects_duplicate_name_on_edit(){
        CategoryId categoryId = category.id();

        EditCategoryCommand command = new EditCategoryCommand(
                categoryId.value().toString(), "Existing name", null, null,
                null,
                null
        );

        when(categoryLoader.load(userId,categoryId)).thenReturn(category);
        doThrow(new ResourceAlreadyExistsException("Category", "name", "Existing name"))
                .when(uniquenessChecker).check(
                        eq(userId),
                        any(CategoryName.class),
                        any(CategoryName.class)
                );

        assertThrows(ResourceAlreadyExistsException.class,
                () -> useCase.execute(command));

        verify(categoryRepository, never()).save(any());
    }

    @Test
    void cat17_rejects_foreign_or_missing_category(){
        CategoryId unexistingId = CategoryId.newId();
        String newName ="New Category name";
        String newDescription = "New Category description";
        String newType = "OUTCOME";

        EditCategoryCommand command = new EditCategoryCommand(
                unexistingId.value().toString(),
                newName,
                newDescription,
                newType,
                null,
                null
        );

        when(categoryLoader.load(userId, unexistingId))
                .thenThrow( new ResourceNotFoundException(
                        "Category",
                        "id",
                        unexistingId.toString()
                ));

        assertThrows(ResourceNotFoundException.class,
                () -> useCase.execute(command));

        verify(categoryLoader).load(userId, unexistingId);
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void cat21_updatesIconAndColor() {
        CategoryId categoryId = category.id();

        EditCategoryCommand command = new EditCategoryCommand(
                categoryId.value().toString(),
                null,
                null,
                null,
                "utensils",
                "amber"
        );

        when(categoryLoader.load(userId, categoryId)).thenReturn(category);
        when(categoryRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CategoryResult result = useCase.execute(command);

        assertEquals("utensils", result.icon());
        assertEquals("amber", result.color());
    }

    @Test
    void cat21_clearsIconAndColorWhenEmpty() {
        CategoryId categoryId = category.id();
        category.changeIcon(CategoryIcon.from("utensils"));
        category.changeColor(CategoryColor.from("amber"));

        EditCategoryCommand command = new EditCategoryCommand(
                categoryId.value().toString(),
                null,
                null,
                null,
                "",
                "   "
        );

        when(categoryLoader.load(userId, categoryId)).thenReturn(category);
        when(categoryRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CategoryResult result = useCase.execute(command);

        assertNull(result.icon());
        assertNull(result.color());
    }

    @Test
    void cat21_keepsIconAndColorWhenAbsent() {
        CategoryId categoryId = category.id();
        category.changeIcon(CategoryIcon.from("utensils"));
        category.changeColor(CategoryColor.from("amber"));

        EditCategoryCommand command = new EditCategoryCommand(
                categoryId.value().toString(),
                null,
                null,
                null,
                null,
                null
        );

        when(categoryLoader.load(userId, categoryId)).thenReturn(category);
        when(categoryRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CategoryResult result = useCase.execute(command);

        assertEquals("utensils", result.icon());
        assertEquals("amber", result.color());
    }

}
