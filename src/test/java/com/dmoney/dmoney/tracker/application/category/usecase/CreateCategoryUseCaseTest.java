package com.dmoney.dmoney.tracker.application.category.usecase;

import com.dmoney.dmoney.shared.domain.models.UserId;
import com.dmoney.dmoney.tracker.application.category.commands.CreateCategoryCommand;
import com.dmoney.dmoney.tracker.application.category.result.CategoryResult;
import com.dmoney.dmoney.shared.domain.exceptions.ResourceAlreadyExistsException;
import com.dmoney.dmoney.tracker.application.port.AuthenticatedUserProvider;
import com.dmoney.dmoney.tracker.domain.category.model.Category;
import com.dmoney.dmoney.tracker.domain.category.model.CategoryName;
import com.dmoney.dmoney.tracker.domain.category.repository.CategoryRepository;
import com.dmoney.dmoney.tracker.domain.category.service.CategoryUniquenessChecker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateCategoryUseCaseTest {

    @Mock
    private CategoryRepository repository;

    @Mock
    private AuthenticatedUserProvider authProvider;

    @Mock
    private CategoryUniquenessChecker uniquenessChecker;

    @InjectMocks
    private CreateCategoryUseCase useCase;

    private UserId userId;

    @BeforeEach
    void setUp(){
        userId = UserId.newId();
        when(authProvider.currentUserId()).thenReturn(userId);
    }


    @Test
    void should_add_category_and_save() {

        CreateCategoryCommand command =
                new CreateCategoryCommand(
                        "new category",
                        "desc",
                        "INCOME"
                );

        when(repository.save(any()))
                .thenAnswer(invocation ->
                        invocation.getArgument(0)
                );

        CategoryResult result = useCase.execute(command);

        verify(uniquenessChecker).check(userId, CategoryName.from("new category"));
        verify(repository).save(any(Category.class));
        assertEquals("new category", result.name());
    }

    @Test
    void should_throw_exception_when_category_already_exists(){
        CreateCategoryCommand categoryCommand =
                new CreateCategoryCommand("existing","desc", "INCOME");

        doThrow(new ResourceAlreadyExistsException("Category", "name", "existing"))
                .when(uniquenessChecker).check(userId, CategoryName.from("existing"));

        assertThrows(ResourceAlreadyExistsException.class,
                () -> useCase.execute(categoryCommand));

        verify(repository, never()).save(any());
    }
}
