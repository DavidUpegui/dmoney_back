package com.dmoney.dmoney.tracker.application.tag.create;

import com.dmoney.dmoney.tracker.application.tag.result.TagResponse;
import com.dmoney.dmoney.shared.domain.exceptions.ResourceAlreadyExistsException;
import com.dmoney.dmoney.tracker.application.tag.command.CreateTagCommand;
import com.dmoney.dmoney.tracker.application.tag.usecase.CreateTagUseCase;
import com.dmoney.dmoney.tracker.domain.tag.model.Tag;
import com.dmoney.dmoney.tracker.domain.tag.model.TagName;
import com.dmoney.dmoney.tracker.domain.tag.repository.TagRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateTagUseCaseTest {

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private CreateTagUseCase useCase;

    @Test
    void should_create_a_tag_and_save(){
        String name = "Name";
        String description = "Description";

        CreateTagCommand command =
                new CreateTagCommand(name, description);

        when(tagRepository.existsByName(any()))
                .thenReturn(false);

        when(tagRepository.save(any(Tag.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        TagResponse result = useCase.execute(command);

        assertEquals(name, result.name());
        assertEquals(description, result.description());
        assertNotNull(result.id());

        verify(tagRepository).existsByName(TagName.from(name));
        verify(tagRepository).save(any(Tag.class));
    }

    @Test
    void should_throw_exception_when_name_already_exists(){
        String name = "Name";
        String description = "Description";

        CreateTagCommand command =
                new CreateTagCommand(name, description);

        when(tagRepository.existsByName(any()))
                .thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class,
                () -> useCase.execute(command));

        verify(tagRepository).existsByName(TagName.from(name));
        verify(tagRepository, never()).save(any(Tag.class));
    }

    @Test
    void should_propagate_exception_when_name_is_null(){
        CreateTagCommand command =
                new CreateTagCommand(null, "description");

        assertThrows(IllegalArgumentException.class,
                () -> useCase.execute(command));

        verify(tagRepository, never()).save(any(Tag.class));
    }

    @Test
    void should_allow_null_description(){
        CreateTagCommand command =
                new CreateTagCommand("Name", null);

        when(tagRepository.existsByName(any()))
                .thenReturn(false);

        when(tagRepository.save(any(Tag.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        TagResponse result = useCase.execute(command);

        assertEquals("Name", result.name());
        assertEquals("", result.description());
        assertNotNull(result.id());

        verify(tagRepository).existsByName(TagName.from("Name"));
        verify(tagRepository).save(any(Tag.class));
    }
}
