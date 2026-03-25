package com.dmoney.dmoney.tracker.application.tag.usecase;


import com.dmoney.dmoney.shared.domain.exceptions.ResourceNotFoundException;
import com.dmoney.dmoney.shared.domain.models.UserId;
import com.dmoney.dmoney.tracker.application.port.AuthenticatedUserProvider;
import com.dmoney.dmoney.tracker.domain.tag.model.TagId;
import com.dmoney.dmoney.tracker.domain.tag.repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteTagUseCaseTest {
    @Mock
    private TagRepository tagRepository;

    @Mock
    private AuthenticatedUserProvider authProvider;

    @InjectMocks
    private DeleteTagUseCase useCase;

    private UserId userId;

    @BeforeEach
    void setUp(){
        userId = UserId.newId();
        when(authProvider.currentUserId()).thenReturn(userId);
    }


    @Test
    void should_delete_tag(){
        TagId tagId = TagId.newId();

        when(tagRepository.deleteByUserIdAndId(userId, tagId))
                .thenReturn(true);

        useCase.execute(tagId.value().toString());

        verify(tagRepository).deleteByUserIdAndId(userId, tagId);
    }

    @Test
    void should_throw_exception_when_tag_not_found(){
        TagId anyId = TagId.newId();

        when(tagRepository.deleteByUserIdAndId(userId, anyId))
                .thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> useCase.execute(anyId.value().toString()));

        verify(tagRepository).deleteByUserIdAndId(userId, anyId);
    }
}
