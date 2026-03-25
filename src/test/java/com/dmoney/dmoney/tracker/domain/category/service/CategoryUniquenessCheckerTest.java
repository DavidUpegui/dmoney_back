package com.dmoney.dmoney.tracker.domain.category.service;

import com.dmoney.dmoney.shared.domain.exceptions.ResourceAlreadyExistsException;
import com.dmoney.dmoney.shared.domain.models.UserId;
import com.dmoney.dmoney.tracker.domain.category.model.CategoryName;
import com.dmoney.dmoney.tracker.domain.category.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryUniquenessCheckerTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryUniquenessChecker checker;

    private UserId userId;

    @BeforeEach
    void setUp() {
        userId = UserId.newId();
    }

    @Test
    void should_throw_exception_when_name_already_exists() {
        CategoryName name = CategoryName.from("Food");

        when(categoryRepository.existsByUserIdAndNameIgnoreCase(userId, name))
                .thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class,
                () -> checker.check(userId, name));
    }

    @Test
    void should_not_throw_exception_when_name_does_not_exist() {
        CategoryName name = CategoryName.from("Food");

        when(categoryRepository.existsByUserIdAndNameIgnoreCase(userId, name))
                .thenReturn(false);

        assertDoesNotThrow(() -> checker.check(userId, name));
    }


    @Test
    void should_check_when_name_is_different() {
        CategoryName newName = CategoryName.from("Transport");
        CategoryName currentName = CategoryName.from("Food");

        when(categoryRepository.existsByUserIdAndNameIgnoreCase(userId, newName))
                .thenReturn(false);

        assertDoesNotThrow(() -> checker.check(userId, newName, currentName));

        verify(categoryRepository).existsByUserIdAndNameIgnoreCase(userId, newName);
    }

    @Test
    void should_not_check_when_name_is_the_same() {
        CategoryName name = CategoryName.from("Food");

        assertDoesNotThrow(() -> checker.check(userId, name, name));

        verify(categoryRepository, never()).existsByUserIdAndNameIgnoreCase(any(), any());
    }

    @Test
    void should_throw_exception_when_new_name_already_exists() {
        CategoryName newName = CategoryName.from("Transport");
        CategoryName currentName = CategoryName.from("Food");

        when(categoryRepository.existsByUserIdAndNameIgnoreCase(userId, newName))
                .thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class,
                () -> checker.check(userId, newName, currentName));
    }
}
