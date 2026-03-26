package com.dmoney.dmoney.tracker.application.category.usecase;

import com.dmoney.dmoney.shared.domain.models.UserId;
import com.dmoney.dmoney.tracker.application.category.result.CategoryResult;
import com.dmoney.dmoney.tracker.application.port.AuthenticatedUserProvider;
import com.dmoney.dmoney.tracker.domain.category.model.Category;
import com.dmoney.dmoney.tracker.domain.category.model.CategoryDescription;
import com.dmoney.dmoney.tracker.domain.category.model.CategoryName;
import com.dmoney.dmoney.tracker.domain.category.model.CategoryType;
import com.dmoney.dmoney.tracker.domain.category.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindAllCategoriesUseCaseTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private AuthenticatedUserProvider authProvider;

    @InjectMocks
    FindAllCategoriesUseCase useCase;

    private UserId userId;

    @BeforeEach
    void setUp(){
        userId = UserId.newId();
        when(authProvider.currentUserId()).thenReturn(userId);
    }

    @Test
    void should_return_all_categories_mapped_to_result() {
        // Arrange
        Category category1 = Category.create(
                userId,
                CategoryName.from("Food"),
                CategoryDescription.from("Food category"),
                CategoryType.INCOME
        );

        Category category2 = Category.create(
                userId,
                CategoryName.from("Tech"),
                CategoryDescription.from("Tech category"),
                CategoryType.OUTCOME
        );

        when(categoryRepository.findAllByUserId(userId))
                .thenReturn(List.of(category1, category2));

        List<CategoryResult> result = useCase.execute();

        assertEquals(2, result.size());
        assertEquals("Food", result.get(0).name());
        assertEquals("Tech", result.get(1).name());

        verify(categoryRepository).findAllByUserId(userId);
    }
    @Test
    void should_return_empty_list_when_no_categories_exist() {
        when(categoryRepository.findAllByUserId(userId))
                .thenReturn(List.of());

        List<CategoryResult> result = useCase.execute();

        assertTrue(result.isEmpty());
        verify(categoryRepository).findAllByUserId(userId);
    }
}
