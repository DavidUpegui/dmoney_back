package com.dmoney.dmoney.tracker.application.tag.usecase;

import com.dmoney.dmoney.shared.domain.models.UserId;
import com.dmoney.dmoney.tracker.application.port.AuthenticatedUserProvider;
import com.dmoney.dmoney.tracker.application.tag.result.TagResponse;
import com.dmoney.dmoney.shared.domain.exceptions.ResourceNotFoundException;
import com.dmoney.dmoney.tracker.domain.tag.model.Tag;
import com.dmoney.dmoney.tracker.domain.tag.model.TagDescription;
import com.dmoney.dmoney.tracker.domain.tag.model.TagId;
import com.dmoney.dmoney.tracker.domain.tag.model.TagName;
import com.dmoney.dmoney.tracker.domain.tag.repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FindTagByIdUseCaseTest {

    @Mock
    private TagRepository tagRepository;

    @Mock
    private AuthenticatedUserProvider authProvider;

    @InjectMocks
    private FindTagByIdUseCase useCase;

    private UserId userId;

    @BeforeEach
    void setUp(){
        userId = UserId.newId();
        when(authProvider.currentUserId()).thenReturn(userId);
    }
    @Test
    void should_found_by_id(){
        Tag tag = Tag.create(
                userId,
                TagName.from("Tag name"),
                TagDescription.from("Tag Description")
        );
        TagId tagId = tag.id();

        when(tagRepository.findByUserIdAndId(userId,tagId))
                .thenReturn(Optional.of(tag));

        TagResponse result = useCase.execute(tagId.value().toString());

        assertEquals(tagId.value().toString(), result.id());
        assertEquals("Tag name", result.name());
        assertEquals("Tag Description", result.description());

        verify(tagRepository).findByUserIdAndId(userId, tagId);
    }

    @Test
    void should_throw_exception_when_not_found(){
        TagId tagId = TagId.newId();

        when(tagRepository.findByUserIdAndId(userId,tagId))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> useCase.execute(tagId.value().toString())
        );

        verify(tagRepository).findByUserIdAndId(userId, tagId);
    }
}
