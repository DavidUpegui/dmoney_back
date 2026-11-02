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
    void cat05_rejects_existing_name() {
        CategoryName name = CategoryName.from("Food");

        when(categoryRepository.existsByUserIdAndNameIgnoreCase(userId, name))
                .thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class,
                () -> checker.check(userId, name));
    }

    @Test
    void cat05_accepts_new_name() {
        CategoryName name = CategoryName.from("Food");

        when(categoryRepository.existsByUserIdAndNameIgnoreCase(userId, name))
                .thenReturn(false);

        assertDoesNotThrow(() -> checker.check(userId, name));
    }


    @Test
    void cat05_checks_changed_name() {
        CategoryName newName = CategoryName.from("Transport");
        CategoryName currentName = CategoryName.from("Food");

        when(categoryRepository.existsByUserIdAndNameIgnoreCase(userId, newName))
                .thenReturn(false);

        assertDoesNotThrow(() -> checker.check(userId, newName, currentName));

        verify(categoryRepository).existsByUserIdAndNameIgnoreCase(userId, newName);
    }

    @Test
    void cat05_skips_same_name() {
        CategoryName name = CategoryName.from("Food");

        assertDoesNotThrow(() -> checker.check(userId, name, name));

        verify(categoryRepository, never()).existsByUserIdAndNameIgnoreCase(any(), any());
    }

    @Test
    void cat05_rejects_existing_changed_name() {
        CategoryName newName = CategoryName.from("Transport");
        CategoryName currentName = CategoryName.from("Food");

        when(categoryRepository.existsByUserIdAndNameIgnoreCase(userId, newName))
                .thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class,
                () -> checker.check(userId, newName, currentName));
    }
}
