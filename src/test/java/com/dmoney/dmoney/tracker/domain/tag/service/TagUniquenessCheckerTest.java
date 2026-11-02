package com.dmoney.dmoney.tracker.domain.tag.service;

import com.dmoney.dmoney.shared.domain.exceptions.ResourceAlreadyExistsException;
import com.dmoney.dmoney.shared.domain.models.UserId;
import com.dmoney.dmoney.tracker.domain.tag.model.TagName;
import com.dmoney.dmoney.tracker.domain.tag.repository.TagRepository;
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
class TagUniquenessCheckerTest {

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private TagUniquenessChecker checker;

    private UserId userId;

    @BeforeEach
    void setUp() {
        userId = UserId.newId();
    }

    @Test
    void tag04_rejects_existing_name() {
        TagName name = TagName.from("Food");

        when(tagRepository.existsByUserIdAndNameIgnoreCase(userId, name))
                .thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class,
                () -> checker.check(userId, name));
    }

    @Test
    void tag04_accepts_new_name() {
        TagName name = TagName.from("Food");

        when(tagRepository.existsByUserIdAndNameIgnoreCase(userId, name))
                .thenReturn(false);

        assertDoesNotThrow(() -> checker.check(userId, name));
    }

    @Test
    void tag09_checks_when_name_changes() {
        TagName newName = TagName.from("Transport");
        TagName currentName = TagName.from("Food");

        when(tagRepository.existsByUserIdAndNameIgnoreCase(userId, newName))
                .thenReturn(false);

        assertDoesNotThrow(() -> checker.check(userId, newName, currentName));

        verify(tagRepository).existsByUserIdAndNameIgnoreCase(userId, newName);
    }

    @Test
    void tag09_skips_check_when_same_name() {
        TagName name = TagName.from("Food");

        assertDoesNotThrow(() -> checker.check(userId, name, name));

        verify(tagRepository, never()).existsByUserIdAndNameIgnoreCase(any(), any());
    }

    @Test
    void tag09_rejects_existing_new_name() {
        TagName newName = TagName.from("Transport");
        TagName currentName = TagName.from("Food");

        when(tagRepository.existsByUserIdAndNameIgnoreCase(userId, newName))
                .thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class,
                () -> checker.check(userId, newName, currentName));
    }
}
