package com.dmoney.dmoney.tracker.application.tag.edit;

import com.dmoney.dmoney.tracker.application.tag.TagResponse;
import com.dmoney.dmoney.tracker.domain.exceptions.ResourceAlreadyExistsException;
import com.dmoney.dmoney.tracker.domain.exceptions.ResourceNotFoundException;
import com.dmoney.dmoney.tracker.domain.tag.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EditTagUseCaseTest {

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private EditTagUseCase useCase;

    private Tag tag;

    @BeforeEach
    void setUp(){
        tag =  Tag.from(
                TagId.newId(),
                TagName.from("Any name"),
                TagDescription.from("Any description")
        );
    }

    @Test
    void should_edit_tag(){
        String id = tag.id().value().toString();
        String changedName = "Changed name";
        String changedDescription = "Changed description";

        TagEditionCommand command = new TagEditionCommand(
                id,
                changedName,
                changedDescription
        );

        when(tagRepository.findById(tag.id()))
                .thenReturn(Optional.of(tag));

        when(tagRepository.existsByName(TagName.from(changedName)))
                .thenReturn(false);

        when(tagRepository.save(any(Tag.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        TagResponse response = useCase.execute(command);

        assertEquals(changedName, response.name());
        assertEquals(changedDescription, response.description());
        assertEquals(id, response.id());

        verify(tagRepository).findById(tag.id());
        verify(tagRepository).existsByName(TagName.from(changedName));
    }

    @Test
    void should_throw_exception_when_tag_to_edit_not_found(){
        String anyId = UUID.randomUUID().toString();
        String changedName = "Changed name";
        String changedDescription = "Changed description";

        TagEditionCommand command = new TagEditionCommand(
                anyId,
                changedName,
                changedDescription
        );

        when(tagRepository.findById(TagId.from(anyId)))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> useCase.execute(command));

        verify(tagRepository).findById(any(TagId.class));
    }

    @Test
    void should_throw_exception_when_other_tag_exists_with_the_changed_name(){
        String id = tag.id().value().toString();
        String existingName = "Existing name";
        String changedDescription = "Changed description";

        TagEditionCommand command = new TagEditionCommand(
                id,
                existingName,
                changedDescription
        );

        when(tagRepository.findById(tag.id()))
                .thenReturn(Optional.of(tag));

        when(tagRepository.existsByName(TagName.from(existingName)))
                .thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class,
                () -> useCase.execute(command));

        verify(tagRepository).findById(any(TagId.class));
    }

    @Test
    void should_not_throw_exception_when_same_tag_exists_with_the_changed_name(){
        String id = tag.id().value().toString();
        String sameName = tag.name().value();
        String changedDescription = "Changed description";

        TagEditionCommand command = new TagEditionCommand(
                id,
                sameName,
                changedDescription
        );

        when(tagRepository.findById(tag.id()))
                .thenReturn(Optional.of(tag));

        when(tagRepository.save(any(Tag.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        TagResponse response = useCase.execute(command);

        assertEquals(sameName, response.name());
        assertEquals(changedDescription, response.description());
        assertEquals(id, response.id());

        verify(tagRepository).findById(tag.id());
        verify(tagRepository, never()).existsByName(any());
    }

    @Test
    void should_edit_only_tag_name(){
        String id = tag.id().value().toString();
        String changedName = "Changed name";
        String sameDescription = tag.description().value();

        TagEditionCommand command = new TagEditionCommand(
                id,
                changedName,
                null
        );

        when(tagRepository.findById(tag.id()))
                .thenReturn(Optional.of(tag));

        when(tagRepository.existsByName(TagName.from(changedName)))
                .thenReturn(false);

        when(tagRepository.save(any(Tag.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        TagResponse response = useCase.execute(command);

        assertEquals(changedName, response.name());
        assertEquals(sameDescription, response.description());
        assertEquals(id, response.id());

        verify(tagRepository).findById(tag.id());
        verify(tagRepository).existsByName(TagName.from(changedName));
    }

    @Test
    void should_only_the_description(){
        String id = tag.id().value().toString();
        String sameName = tag.name().value();
        String changedDescription = "Changed description";

        TagEditionCommand command = new TagEditionCommand(
                id,
                null,
                changedDescription
        );

        when(tagRepository.findById(tag.id()))
                .thenReturn(Optional.of(tag));

        when(tagRepository.save(any(Tag.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        TagResponse response = useCase.execute(command);

        assertEquals(sameName, response.name());
        assertEquals(changedDescription, response.description());
        assertEquals(id, response.id());

        verify(tagRepository).findById(tag.id());
        verify(tagRepository, never()).existsByName(any());
    }
}
