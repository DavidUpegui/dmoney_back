package com.dmoney.dmoney.tracker.application.tag.findById;

import com.dmoney.dmoney.tracker.application.tag.TagResponse;
import com.dmoney.dmoney.tracker.domain.exceptions.ResourceNotFoundException;
import com.dmoney.dmoney.tracker.domain.tag.*;
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

    @InjectMocks
    private FindTagByIdUseCase useCase;

    @Test
    void should_found_by_id(){
        TagId tagId = TagId.newId();

        when(tagRepository.findById(tagId))
                .thenReturn(Optional.of(Tag.from(
                        tagId,
                        TagName.from("Tag name"),
                        TagDescription.from("Tag Description")
                )));

        TagResponse result = useCase.execute(tagId.value().toString());

        assertEquals(tagId.value().toString(), result.id());
        assertEquals("Tag name", result.name());
        assertEquals("Tag Description", result.description());

        verify(tagRepository).findById(tagId);
    }

    @Test
    void should_throw_exception_when_not_found(){
        TagId tagId = TagId.newId();

        when(tagRepository.findById(tagId))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> useCase.execute(tagId.value().toString())
        );

        verify(tagRepository).findById(tagId);
    }
}
