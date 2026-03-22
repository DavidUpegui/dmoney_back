package com.dmoney.dmoney.tracker.application.tag.delete;


import com.dmoney.dmoney.shared.domain.exceptions.ResourceNotFoundException;
import com.dmoney.dmoney.tracker.application.tag.usecase.DeleteTagUseCase;
import com.dmoney.dmoney.tracker.domain.tag.model.TagId;
import com.dmoney.dmoney.tracker.domain.tag.repository.TagRepository;
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

    @InjectMocks
    private DeleteTagUseCase useCase;


    @Test
    void should_delete_tag(){
        TagId tagId = TagId.newId();

        when(tagRepository.existsById(tagId))
                .thenReturn(true);

        useCase.execute(tagId.value().toString());

        verify(tagRepository).existsById(tagId);
        verify(tagRepository).deleteById(tagId);
    }

    @Test
    void should_throw_exception_when_tag_not_found(){
        TagId anyId = TagId.newId();

        when(tagRepository.existsById(anyId))
                .thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> useCase.execute(anyId.value().toString()));

        verify(tagRepository).existsById(anyId);
        verify(tagRepository, never()).deleteById(any());
    }
}
